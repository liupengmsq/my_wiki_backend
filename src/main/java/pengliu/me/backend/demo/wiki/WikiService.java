package pengliu.me.backend.demo.wiki;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import pengliu.me.backend.demo.WikiConfiguration;
import pengliu.me.backend.demo.util.DateTimeUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class WikiService {
    private static final Logger logger = LoggerFactory.getLogger(WikiService.class);

    @Autowired
    private WikiConfiguration wikiConfiguration;

    @Autowired
    private WikiRepository wikiRepository;

    @Autowired
    private WikiCategoryRepository wikiCategoryRepository;

    @Autowired
    private WikiImageRepository wikiImageRepository;

    public List<WikiCategory> getAllWikiCategories() {
        return wikiCategoryRepository.findAll();
    }

    public WikiCategory getDefaultWikiCategory() {
        List<WikiCategory> defaultCategory =  wikiCategoryRepository.findByIsDefault(true);
        Assert.isTrue(defaultCategory.size() > 0 , "找不到默认的wiki分类！！");
        Assert.isTrue(defaultCategory.size() == 1 , "存在多个默认的wiki分类！！");
        return defaultCategory.get(0);
    }

    @Transactional(readOnly = false)
    public void deleteWikiCategoryById(Long id) {
        Optional<WikiCategory> wikiCategoryOption = wikiCategoryRepository.findById(id);
        Assert.isTrue(wikiCategoryOption.isPresent(), "不存在对应的wiki category id");
        Assert.isTrue(!wikiCategoryOption.get().getDefault(), "禁止删除默认的Wiki分类！！");
        wikiCategoryRepository.deleteById(id);
    }

    public List<Wiki> getAllWikiPages() {
        return wikiRepository.findAll();
    }

    @Transactional(readOnly = false)
    public WikiCategory createUpdateWikiCategory(WikiCategory wikiCategory) {
        List<WikiCategory> defaultCategory = wikiCategoryRepository.findByIsDefault(true);
        // 现存数据没有默认分类，允许保存或更新
        if (defaultCategory.size() == 0) {
            return wikiCategoryRepository.save(wikiCategory);
        } else if (wikiCategory.getDefault()) { // 已经存在默认分类，并且当要新建或更新也想成为默认分类时
            if (defaultCategory.get(0).getId().equals(wikiCategory.getId())) { // 如果要更新的就是已经存在的默认分类，允许更新
                if (!wikiCategory.getActive()) {
                    throw new RuntimeException("禁止将默认Wiki分类的条目失效！！");
                }
                return wikiCategoryRepository.save(wikiCategory);
            } else {
                throw new RuntimeException("已经存在默认的Wiki分类！！");
            }
        } else {
            return wikiCategoryRepository.save(wikiCategory);
        }
    }

    public Wiki getWikiById(Long id) {
        Optional<Wiki> wiki = wikiRepository.findById(id);
        Assert.isTrue(wiki.isPresent(), "不存在对应的wiki id");
        return wiki.get();
    }

    @Transactional(readOnly = false)
    public Wiki getWikiById(Long id, Boolean updateAccessInfo) {
        Wiki returnedWiki = getWikiById(id);
        if (!updateAccessInfo) {
            return returnedWiki;
        } else {
            wikiRepository.updateAccessDateTimeAndPageViewedNumberById(id, new Date(), returnedWiki.getPageViewedNumber() + 1);
            return getWikiById(id);
        }
    }

    @Transactional(readOnly = false)
    public void deleteWikiById(Long id) {
        wikiRepository.deleteById(id);
    }

    public WikiCategory getWikiCategoryById(Long id) {
        Optional<WikiCategory> category = wikiCategoryRepository.findById(id);
        Assert.isTrue(category.isPresent(), "不存在对应的wiki id");

        return category.get();
    }

    public WikiCategory getWikiCategoryByName(String categoryName) {
        List<WikiCategory> category = wikiCategoryRepository.findByCategoryName(categoryName);
        Assert.isTrue(category.size() > 0, "不存在对应的wiki category name");
        return category.get(0);
    }

    @Transactional(readOnly = false)
    public Wiki createUpdateWikiPage(Wiki wiki) {
        return wikiRepository.save(wiki);
    }

    public List<WikiImage> getAllWikiImagesByFileName(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return wikiImageRepository.findAll();
        } else {
            return wikiImageRepository.findByFileNameContaining(fileName);
        }
    }

    @Transactional(readOnly = false)
    public String uploadWikiImage(MultipartFile file) throws Exception {
        logger.info("The file is uploading to FTP server.");
        String uploadedFileName = uploadImageToFTPServer(file);
        logger.info("The file \"{}\" has been uploaded to FTP server.", uploadedFileName);
        WikiImage wikiImage = new WikiImage();
        wikiImage.setFileName(uploadedFileName);
        wikiImageRepository.save(wikiImage);
        return uploadedFileName;
    }

    @Transactional(readOnly = false)
    public void deleteWikiImage(String fileName) throws Exception {
        wikiImageRepository.deleteByFileName(fileName);
        deleteImageFromFTPServer(fileName);
    }

    private String uploadImageToFTPServer(MultipartFile file) throws Exception {
        //获取链接对象
        FTPClient ftpClient = new FTPClient();
        int reply;
        boolean uploadFileResult = false;
        String uploadedFileName = null;
        try {
            //连接ftp服务器
            ftpClient.connect(wikiConfiguration.getFtpServerIPAddress(), wikiConfiguration.getFtpServerPort());
            //登录ftp
            ftpClient.login(wikiConfiguration.getFtpUserName(), wikiConfiguration.getFtpUserPassword());
            //返回登录的状态码
            reply = ftpClient.getReplyCode();
            //判断状态码是否正常,连接失败则断开连接
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.error("连接ftp服务器失败!!");
                throw new Exception("连接ftp服务器失败!!");
            }
            //获取字节流
            InputStream inputStream = file.getInputStream();
            //生成文件名
            uploadedFileName = String.format("%s_%s.%s", FilenameUtils.removeExtension(file.getOriginalFilename()),
                    DateTimeUtil.getUTCTimeStamp(),
                    FilenameUtils.getExtension(file.getOriginalFilename()));
            //设置被动模式
            ftpClient.enterLocalPassiveMode();
            //设置为二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //上传文件
            uploadFileResult = ftpClient.storeFile(uploadedFileName, inputStream);
            //关闭字节流
            inputStream.close();
            //退出登录
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("断开ftp服务器连接失败: {}", e.getMessage());
                }
            }
        }
        if (!uploadFileResult) {
            logger.error("上传文件失败!!");
            throw new Exception("上传文件失败!!");
        } else {
            return uploadedFileName;
        }
    }

    private void deleteImageFromFTPServer(String fileName) throws Exception {
        //获取链接对象
        FTPClient ftpClient = new FTPClient();
        int reply;
        boolean deleteFileResult = false;
        String uploadedFileName = null;
        try {
            //连接ftp服务器
            ftpClient.connect(wikiConfiguration.getFtpServerIPAddress(), wikiConfiguration.getFtpServerPort());
            //登录ftp
            ftpClient.login(wikiConfiguration.getFtpUserName(), wikiConfiguration.getFtpUserPassword());
            //返回登录的状态码
            reply = ftpClient.getReplyCode();
            //判断状态码是否正常,连接失败则断开连接
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.error("连接ftp服务器失败!!");
                throw new Exception("连接ftp服务器失败!!");
            }
            //设置被动模式
            ftpClient.enterLocalPassiveMode();
            //设置为二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //上传文件
            deleteFileResult = ftpClient.deleteFile(fileName);
            //退出登录
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("断开ftp服务器连接失败: {}", e.getMessage());
                }
            }
        }
        if (!deleteFileResult) {
            logger.error("删除文件失败!!");
            throw new Exception("删除文件失败!!");
        }
    }
}

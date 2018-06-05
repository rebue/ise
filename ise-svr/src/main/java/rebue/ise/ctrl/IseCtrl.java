package rebue.ise.ctrl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rebue.ise.dic.UploadResultDic;
import rebue.ise.ro.UploadRo;
import rebue.wheel.RandomEx;

@RestController
public class IseCtrl {
    private final static Logger _log = LoggerFactory.getLogger(IseCtrl.class);

    /**
     * 文件存储的根路径
     */
    @Value("${ise.rootPath:./static/files/}")
    private String              rootPath;

    @PostMapping("/ise/upload")
    public UploadRo uploadFile(MultipartFile[] multipartFile, HttpServletRequest req) throws IllegalStateException, IOException {
        _log.info("\r\n============================= 开始上传文件 =============================\r\n");
        try {
            String moduleName = req.getParameter("moduleName");
            _log.info("模块名称: {}", moduleName);
            _log.info("文件的数量: {}", multipartFile.length);
            UploadRo ro = new UploadRo();
            if (multipartFile.length == 0) {
                ro.setResult(UploadResultDic.FAIL);
                ro.setMsg("没有上传文件");
                return ro;
            }
            ro.setFilePaths(new LinkedList<>());
            for (MultipartFile file : multipartFile) {
                // 获取原始文件的名字
                String originfileName = file.getOriginalFilename();
                _log.info("原始文件名字为:{}", originfileName);
                // 获取上传文件的类型
                String fileType = originfileName.substring(originfileName.lastIndexOf("."));
                _log.info("文件类型为:{}", fileType);
                // 新的文件名字
                String newFileName = RandomEx.randomUUID() + fileType;
                _log.info("新文件名字为:{}", newFileName);
                SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/HH/mm/");// 设置日期格式
                String path = sdf.format(new Date());
                // 相对路径
                String fileRelPath = "/" + moduleName + path;
                // 绝对路径
                String fileAbsPath = rootPath + fileRelPath;
                _log.info("文件上传的路径为:{}", fileAbsPath);
                File dir = new File(fileAbsPath);
                _log.info("开始判断文件夹是否存在");
                // 判断文件夹是否存在
                if (!dir.exists()) {
                    _log.info("上传文件时发现文件夹不存在,开始创建文件夹");
                    // 设置写权限，windows下不用此语句
                    dir.setWritable(true, false);
                    // 如果不存在则创建文件夹
                    dir.mkdirs();
                    _log.info("上传文件时发现文件夹不存在,创建文件夹成功");
                }
                _log.info("开始保存文件到服务器");
                File saveFile = new File(dir.getAbsoluteFile(), newFileName);
                file.transferTo(saveFile);
                ro.getFilePaths().add(fileRelPath + newFileName);
            }

            ro.setResult(UploadResultDic.SUCCESS);
            ro.setMsg("上传文件成功");
            return ro;
        } finally {
            _log.info("\r\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 结束上传文件 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n");
        }
    }
}

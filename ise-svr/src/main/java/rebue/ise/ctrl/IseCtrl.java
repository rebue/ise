package rebue.ise.ctrl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rebue.ise.dic.UploadResultDic;
import rebue.ise.ro.SaveFileRo;
import rebue.ise.ro.UploadRo;
import rebue.ise.to.SaveFileTo;
import rebue.wheel.RandomEx;

@RestController
public class IseCtrl {
	private final static Logger _log = LoggerFactory.getLogger(IseCtrl.class);

	/**
	 * 文件存储的根路径
	 */
	@Value("${ise.rootPath:./static/files/}")
	private String rootPath;

	/**
	 * 上传文件
	 * 
	 * @param multipartFile
	 * @param req
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@PostMapping("/ise/upload")
	public UploadRo uploadFile(MultipartFile[] multipartFile, HttpServletRequest req)
			throws IllegalStateException, IOException {
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
				String newFileName = RandomEx.randomUUID();
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

	/**
	 * 保存文件
	 * 
	 * @param to
	 * @return
	 */
	@PostMapping("/ise/save")
	public SaveFileRo saveFile(@RequestBody SaveFileTo to) {
		_log.info("保存文件的请求参数为：{}", to);
		SaveFileRo ro = new SaveFileRo();
		if (StringUtils.isAnyBlank(to.getContent(), to.getFileType())) {
			_log.error("保存文件出现参数错误，请求的参数为：{}", to);
			ro.setResult(0);
			ro.setMsg("参数错误");
			return ro;
		}

		if (to.getOldFilePath() != null) {
			String oldPath = rootPath + "/" + to.getOldFilePath();
			File file = new File(oldPath);
			if (file.exists()) {
				_log.info("保存文件时发现有旧的文件，开始删除文件");
				file.delete();
			}
		}

		// 新的文件名字
		String newFileName = RandomEx.randomUUID();
		SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/HH/mm/");// 设置日期格式
		String path = sdf.format(new Date());
		// 文件相对路径
		String fileRelPath = "goodsDetail" + path + newFileName + "." + to.getFileType();
		// 文件绝对路径
		String pathName = rootPath + "/" + fileRelPath;
		_log.info("保存文件的文件路径和文件名称为：{}", pathName);
		File file = new File(pathName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		try {
			file.createNewFile();
		} catch (IOException e) {
			_log.error("保存文件出现异常，{}", e);
			e.printStackTrace();
		}

		try {
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(pathName)), "utf-8"));
			bw.write(to.getContent());
			bw.flush();
			bw.close();

			_log.info("保存文件成功，请求的参数为：{}", to);
			ro.setResult(1);
			ro.setMsg("保存成功");
			ro.setFilePath(fileRelPath);
			return ro;
		} catch (Exception e) {
			_log.error("保存文件写入数据出现异常，{}", e);
			ro.setResult(-1);
			ro.setMsg("保存失败");
			return ro;
		} finally {
			_log.info("\r\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 结束保存文件 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n");
		}
	}

	/**
	 * 按照字节读取文件内容
	 * 
	 * @return
	 */
	@GetMapping("/ise/read")
	public String readFileByByte(@RequestParam("filePath") String filePath) {
		_log.info("开始按字节读取文件，请求的参数为：{}", filePath);
		String result = "";
		File file = new File(rootPath + "/" + filePath);
		InputStream is = null;
		try {
			is = new FileInputStream(file);

			byte[] tempbytes = new byte[1024]; // 数组大小由文件决定
			int tempByte; // 接收每一个读取进来的数据
			while ((tempByte = is.read(tempbytes)) != -1) {
				// 将字节转为字符
				result += new String(tempbytes, 0, tempByte);
			}

			is.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		} finally {
			_log.info("根据字节读取文件的返回值为：{}", result);
		}
	}
}

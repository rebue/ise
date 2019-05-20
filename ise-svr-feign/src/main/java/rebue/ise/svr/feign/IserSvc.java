package rebue.ise.svr.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import rebue.ise.ro.SaveFileRo;
import rebue.ise.to.SaveFileTo;
import rebue.sbs.feign.FeignConfig;

@FeignClient(name = "ise-svr", configuration = FeignConfig.class)
public interface IserSvc {

	/**
	 * 保存文件
	 * 
	 * @param to
	 * @return
	 */
	@PostMapping("/ise/save")
	SaveFileRo saveFile(@RequestBody SaveFileTo to);
	
	/**
	 * 按照字节读取文件内容
	 * 
	 * @return
	 */
	@GetMapping("/ise/read")
	String readFileByByte(@RequestParam("filePath") String filePath);
}

package rebue.ise.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 保存文件请求参数
 * 
 * @author lbl
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class SaveFileTo {

	/**
	 * 保存内容
	 */
	private String content;

	/**
	 * 旧文件路径
	 */
	private String oldFilePath;
	
	/**
	 * 模块名称
	 */
	private String moduleName;

	/**
	 * 文件类型
	 */
	private String fileType;
}

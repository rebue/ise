package rebue.ise.ro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 保存文件返回值
 * @author lbl
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class SaveFileRo {

	/**
	 * 保存文件返回值（1：成功  0：参数错误 -1：失败）
	 */
	private Integer result;
	
	/**
	 * 保存文件返回值说明
	 */
	private String msg;
	
	/**
	 * 保存文件的路径（只有成功才返回）
	 */
	private String filePath;
}

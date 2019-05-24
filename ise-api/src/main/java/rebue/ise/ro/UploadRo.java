package rebue.ise.ro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.ise.dic.UploadResultDic;

/**
 * 上传文件的返回结果
 */
@JsonInclude(Include.NON_NULL)
public class UploadRo {
    /**
     * 上传文件返回结果的代码
     */
    private UploadResultDic result;
    /**
     * 上传文件返回结果的代码信息
     */
    private String          msg;

    /**
     * 上传文件的路径
     */
    private List<String>    filePaths;

    public UploadResultDic getResult() {
        return result;
    }

    public void setResult(UploadResultDic result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }

    @Override
    public String toString() {
        return "UploadRo [result=" + result + ", msg=" + msg + ", filePaths=" + filePaths + "]";
    }

}

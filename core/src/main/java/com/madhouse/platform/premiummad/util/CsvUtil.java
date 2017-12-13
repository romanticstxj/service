package com.madhouse.platform.premiummad.util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.exception.BusinessException;

public class CsvUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);
	
	public static <T> File createCSVFile(List<T> exportData, List<String> headers, List<String> fileds,  
			String outPutPath, String csvFileName, Class<?> T, boolean overlay) {  
		//?提前判断exportData为空
        File csvFile = null;  
        BufferedWriter csvFileOutputStream = null;  
        try {  
            File file = new File(outPutPath);  
            if (!file.exists()) { 
            	if(!file.mkdirs()){
            		logger.debug("创建文件夹" + outPutPath + "失败！");
            		return null;
            	}
            }  
            // 定义文件名格式并创建  
            csvFile = new File(csvFileName);
            if(csvFile.exists()){ // 判断目标文件是否存在
            	if(overlay){
            		logger.debug(csvFileName + "已存在，准备删除它！");
                    if (!csvFile.delete()) {
                        logger.error("删除目标文件" + csvFileName + "失败！");
                        return null;
                    }
            	} else{
            		logger.error("目标文件" + csvFileName + "已存在！");
                    return null;
            	}
            }
            
            if(!csvFile.createNewFile()){
            	logger.error("创建csv文件" + csvFileName + "失败！");
            	return null;
            }
            logger.debug("成功创建csv文件：" + csvFileName);  
            
            // UTF-8使正确读取分隔符","  
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(  
                    new FileOutputStream(csvFile), "UTF-8"), 1024);  
            logger.debug("csvFileOutputStream：" + csvFileOutputStream);  
            // 写入文件头部 
            for(String header: headers){
            	csvFileOutputStream  
                .write(header != null ? new String(  
                        header.getBytes("UTF-8"), "UTF-8") : "");  
            	csvFileOutputStream.write(",");  
            }
            csvFileOutputStream.write("\r\n");  
            // 写入文件内容,  
            // ============ //第一种格式：Arraylist<实体类>填充实体类的基本信息==================  
            for (int j = 0; exportData != null && !exportData.isEmpty()  
                    && j < exportData.size(); j++) {  
            	T t = exportData.get(j);  
                String[] contents = new String[fileds.size()];  
                for (int i = 0; fileds != null && i < fileds.size(); i++) {  
                    String filedName = toUpperCaseFirstOne(fileds.get(i));  
                    Method method = T.getMethod(filedName);  
                    method.setAccessible(true);  
                    Object obj = method.invoke(t);  
                    String str = String.valueOf(obj);  
                    if (str == null || str.equals("null"))  
                        str = "";  
                    contents[i] = str;  
  
                }  
  
                for (int n = 0; n < contents.length; n++) {  
                    // 将生成的单元格添加到工作表中  
                    csvFileOutputStream.write(contents[n]);  
                    csvFileOutputStream.write(",");  
  
                }  
                csvFileOutputStream.write("\r\n");  
            }  
  
            // ============ //第二种格式：Arraylist<map>填充实体类的基本信息==================  
            // for (Iterator iterator = exportData.iterator();  
            // iterator.hasNext();) {  
            // Object row = (Object) iterator.next();  
            // for (Iterator propertyIterator = map.entrySet().iterator();  
            // propertyIterator  
            // .hasNext();) {  
            // java.util.Map.Entry propertyEntry = (java.util.Map.Entry)  
            // propertyIterator  
            // .next();  
            // csvFileOutputStream  
            // .write((String) BeanUtils.getProperty(  
            // row,  
            // ((String) propertyEntry.getKey()) != null ? (String)  
            // propertyEntry  
            // .getKey() : ""));  
            // if (propertyIterator.hasNext()) {  
            // csvFileOutputStream.write(",");  
            // }  
            // }  
            // if (iterator.hasNext()) {  
            // csvFileOutputStream.write("\r\n");  
            // }  
            // }  
  
            // =================================  
            csvFileOutputStream.flush();  
        } catch (Exception e) {  
            return null;
        } finally {  
            try {  
            	if(csvFileOutputStream != null){
            		csvFileOutputStream.close();  
            	}
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return csvFile;  
    }  
  
    /** 
     * 下载文件 
     *  
     * @param response 
     * @param csvFilePath 
     *            文件路径 
     * @param fileName 
     *            文件名称 
     * @throws IOException 
     */  
    public static void exportFile(HttpServletResponse response,  
            String csvFilePath, String fileName) throws IOException {  
        response.setContentType("application/csv;charset=GBK");  
        response.setHeader("Content-Disposition", "attachment;  filename="  
                + new String(fileName.getBytes("GBK"), "ISO8859-1"));  
        // URLEncoder.encode(fileName, "GBK")  
  
        InputStream in = null;  
        try {  
            in = new FileInputStream(csvFilePath);  
            int len = 0;  
            byte[] buffer = new byte[1024];  
            response.setCharacterEncoding("GBK");  
            OutputStream out = response.getOutputStream();  
            while ((len = in.read(buffer)) > 0) {  
                // out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF  
                // });  
                out.write(buffer, 0, len);  
            }  
        } catch (FileNotFoundException e) {  
        	logger.error(e.getMessage());  
        } finally {  
            if (in != null) {  
                try {  
                    in.close();  
                } catch (Exception e) {  
                    throw new RuntimeException(e);  
                }  
            }  
        }  
    }  
  
    /** 
     * 删除该目录filePath下的所有文件 
     *  
     * @param filePath 
     *            文件目录路径 
     */  
    public static void deleteFiles(String filePath) {  
        File file = new File(filePath);  
        if (file.exists()) {  
            File[] files = file.listFiles();  
            for (int i = 0; i < files.length; i++) {  
                if (files[i].isFile()) {  
                    files[i].delete();  
                }  
            }  
        }  
    }  
  
    /** 
     * 删除单个文件 
     *  
     * @param filePath 
     *            文件目录路径 
     * @param fileName 
     *            文件名称 
     */  
    public static void deleteFile(String filePath, String fileName) {  
        File file = new File(filePath);  
        if (file.exists()) {  
            File[] files = file.listFiles();  
            for (int i = 0; i < files.length; i++) {  
                if (files[i].isFile()) {  
                    if (files[i].getName().equals(fileName)) {  
                        files[i].delete();  
                        return;  
                    }  
                }  
            }  
        }  
    }  
  
    /** 
     * 将第一个字母转换为大写字母并和get拼合成方法 
     *  
     * @param origin 
     * @return 
     */  
    private static String toUpperCaseFirstOne(String origin) {  
        StringBuffer sb = new StringBuffer(origin);  
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));  
        sb.insert(0, "get");  
        return sb.toString();  
    }  
    
    public static ByteArrayOutputStream process(String reportUri)throws IOException{
        File f = new File(reportUri);  
        if (!f.exists()) {  
            throw new FileNotFoundException(reportUri);  
        } 
        if(!f.canRead()){
        	throw new BusinessException(StatusCode.SC20705);
        }
  
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());  
        BufferedInputStream in = null;  
        try {  
            in = new BufferedInputStream(new FileInputStream(f));  
            int buf_size = 1024;  
            byte[] buffer = new byte[buf_size];  
            int len = 0;  
            while(-1 != (len = in.read(buffer, 0, buf_size))){
            	bos.write(buffer, 0, len);
            }
            return bos;  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        } finally {  
            try {  
                in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            logger.debug("bos close()!");
            bos.close();  
        }  
    }
}

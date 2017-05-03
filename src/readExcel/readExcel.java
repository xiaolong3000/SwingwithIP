package readExcel;

/**
 * 读取excel中的数据导入数据库
 */


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.util.List;

import java.util.regex.Pattern;



import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DateUtil;



public class readExcel {



public String[][] result(String path){
    File file=new File(path);
    String[][] r=null;
    try {
        r=getData(file,1);

    } catch (IOException e) {
        e.printStackTrace();
    }
      return r;
}




    /**
     *
     * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
     *
     * @param file
     *            读取数据的源Excel
     *
     * @param ignoreRows
     *            读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
     *            不知原因，至少需要忽略一行，所以要对excel表进行一定的修改
     *
     * @return 读出的Excel中数据的内容
     *
     * @throws FileNotFoundException
     *
     * @throws IOException
     */

    public  String[][] getData(File file, int ignoreRows)

            throws FileNotFoundException, IOException {

        List<String[]> result = new ArrayList<String[]>();

        int rowSize = 0;

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(

                file));

        // 打开HSSFWorkbook

        POIFSFileSystem fs = new POIFSFileSystem(in);

        HSSFWorkbook wb = new HSSFWorkbook(fs);

        HSSFCell cell = null;

        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {

            HSSFSheet st = wb.getSheetAt(sheetIndex);

            // 第一行为标题，不取

            for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {

                HSSFRow row = st.getRow(rowIndex);

                if (row == null) {

                    continue;

                }

                int tempRowSize = row.getLastCellNum() + 1;

                if (tempRowSize > rowSize) {

                    rowSize = tempRowSize;

                }

                String[] values = new String[rowSize];

                Arrays.fill(values, "");

                boolean hasValue = false;

                for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {

                    String value = "";

                    cell = row.getCell(columnIndex);

                    if (cell != null) {

                        // 注意：一定要设成这个，否则可能会出现乱码

                        //cell.setEncoding(HSSFCell.ENCODING_UTF_16);

                        switch (cell.getCellType()) {

                            case HSSFCell.CELL_TYPE_STRING:

                                value = cell.getStringCellValue();

                                break;

                            case HSSFCell.CELL_TYPE_NUMERIC:
							/*					short format = cell.getCellStyle().getDataFormat();
						    SimpleDateFormat sdf = null;
						    if(format == 14 || format == 31 || format == 57 || format == 58){
						        //日期
						        sdf = new SimpleDateFormat("yyyy-MM-dd");
						    }else if (format == 20 || format == 32) {
						        //时间
						        sdf = new SimpleDateFormat("HH:mm");
						    }
						    double value1 = cell.getNumericCellValue();
						    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value1);
						    value = sdf.format(date);
           */
						/*	if (HSSFDateUtil.isCellDateFormatted(cell)) {

								Date date = cell.getDateCellValue();

								if (date != null) {

									value = new SimpleDateFormat("HH:mm")

									.format(date);


								} else {
									value = "";

								}

							} else {

								value = new DecimalFormat("0").format(cell

								.getNumericCellValue());

							}*/



                                if(HSSFDateUtil.isCellDateFormatted(cell)){
                                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                                    value=sdf.format(cell.getDateCellValue());
                                    //value=""+DateUtil.getExcelDate(cell.getDateCellValue());
                                }else{
                                    value=""+cell.getNumericCellValue();
                                }
                                //value=""+cell.getNumericCellValue();

                                break;


                            case HSSFCell.CELL_TYPE_FORMULA:

                                // 导入时如果为公式生成的数据则无值

							/*if (!cell.getStringCellValue().equals("")) {

								value = cell.getStringCellValue();

							} else {

								value = cell.getNumericCellValue() + "";

							}
							try{
								value=String.valueOf(cell.getNumericCellValue());
							}catch(IllegalStateException e){
								value=String.valueOf(cell.getRichStringCellValue());
							}
							*/

                                value=""+cell.getCellFormula();

                                break;

                            case HSSFCell.CELL_TYPE_BLANK:
                                value="";

                                break;

                            case HSSFCell.CELL_TYPE_ERROR:

                                value = "";

                                break;

                            case HSSFCell.CELL_TYPE_BOOLEAN:

                                value = (cell.getBooleanCellValue() == true ? "Y"

                                        : "N");

                                break;

                            default:

                                value = "";

                        }

                    }

                    if (columnIndex == 0 && value.trim().equals("")) {

                        break;

                    }

                    values[columnIndex] = rightTrim(value);

                    hasValue = true;

                }

                if (hasValue) {

                    result.add(values);

                }

            }

        }

        in.close();

        String[][] returnArray = new String[result.size()][rowSize];

        for (int i = 0; i < returnArray.length; i++) {

            returnArray[i] = (String[]) result.get(i);

        }

        return returnArray;

    }

    /**
     *
     * 去掉字符串右边的空格
     *
     * @param str
     *            要处理的字符串
     *
     * @return 处理后的字符串
     */







    public static String rightTrim(String str) {

        if (str == null) {

            return "";

        }

        int length = str.length();

        for (int i = length - 1; i >= 0; i--) {

            if (str.charAt(i) != 0x20) {

                break;

            }

            length--;

        }

        return str.substring(0, length);

    }
    /*
     *
     * 判断是否为数字
     *
     * */
    public static boolean isNumeric(String str){
        if(str==null||"".equals(str.trim()) || str.length()>10) return false;
        Pattern pattern = Pattern.compile("^0|[1-9]\\d*(\\.\\d+)?$");
        return pattern.matcher(str).matches();
    }
    /*
     * 读取并修改cell
     *
     * */
    public static void oCell(HSSFRow row,short column,String value){
        HSSFCell cell=row.getCell(column);
        //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
        cell.setCellValue(value);
    }

	/*
	 *
	 *
	 * */

}

package xlbeans;

import java.io.IOException;
import java.nio.file.Paths;

import xlbeans.beans.TestBean;
import xlbeans.exception.XlBeansException;
import xlbeans.exception.XlBeansIllegalExcelBeanException;
import xlbeans.exception.XlBeansIllegalFileFormatException;

public class Test {

	public static void main(String[] args) {

		try {
			Mapper<TestBean> mapper = new Mapper<>();
			System.out.println(
					mapper.map(Paths.get(Test.class.getClassLoader().getResource("data.xlsx").getFile().substring(1)),
							TestBean.class));
			mapper.close();
		} catch (XlBeansIllegalFileFormatException e) {
			e.printStackTrace();
		} catch (XlBeansIllegalExcelBeanException e) {
			e.printStackTrace();
		} catch (XlBeansException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

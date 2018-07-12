
package au.com.suttons.notification.fileprocessor;

import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.suttons.notification.bean.EmployeeFileDetailBean;
import au.com.suttons.notification.util.CsvHandler;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTerminationFileProcessor extends CsvHandler implements FileProcessor
{
	@SuppressWarnings({"rawtypes", "unchecked"})
	public  ColumnPositionMappingStrategy setColumMapping() {

		ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
		strategy.setType(EmployeeFileDetailBean.class);
		String[] columns = new String[] {"status","employeeNumber","firstName","lastName","description","position","terminationDate"};
		strategy.setColumnMapping(columns);
		return strategy;
	}

	public <T> List<T> cleanUp(List<T> list) {

		List<EmployeeFileDetailBean> newList = null;

		if (list != null && !list.isEmpty()) {

			newList = new ArrayList<EmployeeFileDetailBean>();

			for (Object obj : list) {

				EmployeeFileDetailBean bean = (EmployeeFileDetailBean) obj;

				if (StringUtils.isNotBlank(bean.getEmployeeNumber())) {
					newList.add(bean);
				}

			}
		}

		return (List<T>) newList;
	}

}
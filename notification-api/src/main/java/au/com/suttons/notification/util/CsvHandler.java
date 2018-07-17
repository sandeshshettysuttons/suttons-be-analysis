package au.com.suttons.notification.util;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import java.io.FileReader;
import java.util.List;

public abstract class CsvHandler 
{
	public  List<Object> getList(String fileName)
	{
		List<Object> list = null;
		FileReader fileReader = null;
		CSVReader reader = null;
		
		try
		{
			CsvToBean csvBean = new CsvToBean();
			fileReader = new FileReader(fileName);
			reader = new CSVReader(fileReader);
			list = csvBean.parse(setColumMapping(), reader);
			//Remove header
			list.remove(0);
			list = cleanUp(list);
		}	
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally {
			if (fileReader !=null)
			{
				try {
					fileReader.close();
					if (reader != null) {
						reader.close();
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}	
		}
		return list;
	}
	
	//implement only this method as in SrvRecallHander class
    @SuppressWarnings("rawtypes")
	public abstract ColumnPositionMappingStrategy setColumMapping();
    public abstract  <T> List<T> cleanUp(List<T> list);
}

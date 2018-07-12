package au.com.suttons.notification.util;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.Map.Entry;

/**
 * Utility class for handling Value Objects.
 */
public final class VOUtils
{
	public static final String NO_CHANGE = "No Change";
	/** logging instance */
	private static final Log LOG = LogFactory.getLog(VOUtils.class);

	/**
	 * Constructor.
	 */
	private VOUtils()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Generates a List of Strings highlighting the difference of two Objects.
	 * @param newBean the first Object in the comparison
	 * @param oldBean the second Object in the comparison
	 * @return the diff of two beans using no prefix, and the Audit log delimiter
	 */
	public static List diffBeans(Object newBean, Object oldBean)
	{
		List beanDiff = new ArrayList();
		getBeanDifference(beanDiff, "", newBean, oldBean);
		if (beanDiff.isEmpty())
		{
			beanDiff.add(NO_CHANGE);
		}
		return beanDiff;
	}

	/**
	 * Differences are of the form: "<prefix>
	 * 
	 *  &lt;propertyName&gt; changed from '&lt;oldValue&gt;' to '&lt;newValue&gt;'&quot;
	 *                  If the oldValue is null, the message will be
	 *                  &quot; &lt;prefix&gt; &lt;propertyName&gt; set to '&lt;newValue&gt;'&quot;
	 *                  If the newValue is null, the message will be
	 *                  &quot; &lt;prefix&gt; &lt;propertyName&gt; removed&quot;
	 *                 
	 *                  @param prefix message prefix
	 *                  @param argNewBean the first Object in the comparison
	 *                  @param argOldBean the second Object in the comparison
	 *                  @param difference the list of differeces
	 * 
	 */
	public static void getBeanDifference(List difference, String prefix, Object argNewBean, Object argOldBean)
	{
		Object newBean = nullEmptyString(argNewBean);
		Object oldBean = nullEmptyString(argOldBean);

		if (newBean != null && oldBean != null)
		{
			// if we aren't going to get property differences for this type, just add a change message
			if (!drillDown(newBean))
			{
				if (!newBean.equals(oldBean))
				{
					getBeanChangedMessage(difference, prefix, oldBean, newBean);
				}
				return;
			}

			// if the type is a list process elements within list for differences
			if (newBean instanceof List)
			{
				List list = (List) newBean;
				if (!list.isEmpty())
				{
					processList(difference, prefix, newBean, oldBean);
				}
			}
			// if the type is a map process elements within map for differences
			else if (newBean instanceof Map)
			{
				if (!((Map) newBean).isEmpty() && !((Map) oldBean).isEmpty())
				{
					processMap(difference, prefix, newBean, oldBean);
				}
				else if (!((Map) newBean).isEmpty())
				{
					processMap(difference, prefix, newBean, null);
				}
				else if (!((Map) oldBean).isEmpty())
				{
					processMap(difference, prefix, null, oldBean);
				}
			}
			else
			{
				BeanMap newBeanMap = new BeanMap(newBean);
				BeanMap oldBeanMap = new BeanMap(oldBean);
				// for all in new Bean Map
				//   if the value is a primitive and not equal to the old bean map, append a string
				//   if the value is not a primitive and not equal, call getBeanDifference recursively
				//		with a new prefix of the current prefix and property name

				String propertyName = null;
				Object newValue = null;
				Object oldValue = null;
				String newPrefix = null;
				for (Iterator iter = newBeanMap.keyIterator(); iter.hasNext();)
				{
					propertyName = (String) iter.next();

					if (isSkippedProperty(newBean, propertyName))
					{
						continue;
					}

					newValue = newBeanMap.get(propertyName);
					oldValue = oldBeanMap.get(propertyName);
					newPrefix = StringUtils.isEmpty(prefix) ? propertyName : prefix + "." + propertyName;
					getBeanDifference(difference, newPrefix, newValue, oldValue);
				}
			}
		}
		else if (newBean != null && oldBean == null)
		{
			// if we aren't going to get property differences for this type, just add a change message
			if (!drillDown(newBean))
			{
				getBeanAddedMessage(difference, prefix, newBean);
				return;
			}
			// if the type is a list process elements within list for differences
			if (newBean instanceof List)
			{
				List list = (List) newBean;
				if (!list.isEmpty())
				{
					processList(difference, prefix, newBean, null);
				}
			}
			// if the type is a map process elements within map for differences
			else if (newBean instanceof Map)
			{
				if (!((Map) newBean).isEmpty())
				{
					processMap(difference, prefix, newBean, null);
				}
			}
			else
			{
				// generate 'set' message for all properties of new bean, recursively
				BeanMap beanMap = new BeanMap(newBean);
				for (Iterator iter = beanMap.keyIterator(); iter.hasNext();)
				{
					String propertyName = (String) iter.next();
					if (!isSkippedProperty(newBean, propertyName))
					{
						String newPrefix = StringUtils.isEmpty(prefix) ? propertyName : prefix + "." + propertyName;
						getBeanDifference(difference, newPrefix, beanMap.get(propertyName), null);
					}
				}
			}
		}
		else if (newBean == null && oldBean != null)
		{
			// if we aren't going to get property differences for this type, just add a remove message
			if (!drillDown(oldBean))
			{
				getRemovedBeanMessage(difference, prefix, oldBean);
				return;
			}
			// if the type is a list process elements within list for differences
			if (oldBean instanceof List)
			{
				List list = (List) oldBean;
				if (!list.isEmpty())
				{
					processList(difference, prefix, null, oldBean);
				}
			}
			// if the type is a map process elements within map for differences
			else if (oldBean instanceof Map)
			{
				if (!((Map) oldBean).isEmpty())
				{
					processMap(difference, prefix, null, oldBean);
				}
			}
			else
			{
				// generate message for all properties of delete bean, recursively
				BeanMap beanMap = new BeanMap(oldBean);
				for (Iterator iter = beanMap.keyIterator(); iter.hasNext();)
				{
					String propertyName = (String) iter.next();
					if (!isSkippedProperty(oldBean, propertyName))
					{
						String newPrefix = StringUtils.isEmpty(prefix) ? propertyName : prefix + "." + propertyName;
						getBeanDifference(difference, newPrefix, null, beanMap.get(propertyName));
					}
				}
			}
		}
	}

	/**
	 * If the passed in bean is a String, and it just contains a blank or empty string, then this method will turn it
	 * into null to avoid unwanted messages.
	 * @param bean the Object to check
	 * @return either the unaltered object, or a nulled String.
	 */
	private static Object nullEmptyString(Object bean)
	{
		if (bean instanceof String)
		{
			if (StringUtils.isBlank((String) bean))
			{
				return null;
			}
			else
			{
				return ((String) bean).trim();
			}
		}
		return bean;
	}

	/**
	 * Process through the list elements for differences in properties
	 * @param prefix message prefix
	 * @param newBean the first Object in the comparison
	 * @param oldBean the second Object in the comparison
	 * @param difference the list of differeces
	 */
	private static void processList(List difference, String prefix, Object newBean, Object oldBean)
	{
		List newList = (List) newBean;
		List oldList = (List) oldBean;
		Object newValue = null;
		Object oldValue = null;
		int savedi = 0;
		int newListSize = newList == null ? 0 : newList.size();
		if (oldList != null)
		{
			savedi = oldList.size();
			for (int i = 0; i < oldList.size(); i++)
			{
				oldValue = (Object) oldList.get(i);
				if (i < newListSize)
				{
					newValue = (Object) newList.get(i);
					// changed element
					getBeanDifference(difference, prefix, newValue, oldValue);

				}
				else
				{
					// removed element
					getBeanDifference(difference, prefix, null, oldValue);
				}
			}
		}
		for (int i = savedi; i < newListSize; i++)
		{
			newValue = (Object) newList.get(i);
			// added element
			getBeanDifference(difference, prefix, newValue, null);
		}
	}

	/**
	 * Process through the map elements for differences in properties
	 * @param prefix message prefix
	 * @param newBean the first Object in the comparison
	 * @param oldBean the second Object in the comparison
	 * @param difference the list of differeces
	 */
	private static void processMap(List difference, String prefix, Object newBean, Object oldBean)
	{
		Map newMap = (Map) newBean;
		Map oldMap = (Map) oldBean;

		if (oldMap == null)
		{
			oldMap = new HashMap(0);
		}
		
		if (newMap == null)
		{
			newMap = new HashMap(0);
		}
		
		//process changed and removed elements
		for (Iterator itr = oldMap.entrySet().iterator(); itr.hasNext();)
		{
			Entry entry = (Entry) itr.next();
			Object oldValue = entry.getValue();
			Object newValue = newMap.get(entry.getKey());
			getBeanDifference(difference, StringUtils.isEmpty(prefix) ? "" + entry.getKey() : prefix + "." + entry.getKey(), newValue, oldValue);
		}
		
		//process new elements
		for (Iterator itr = newMap.entrySet().iterator(); itr.hasNext();)
		{
			Entry entry = (Entry) itr.next();
			if (!oldMap.containsKey(entry.getKey()))
			{
				getBeanDifference(difference, StringUtils.isEmpty(prefix) ? "" + entry.getKey() : prefix + "." + entry.getKey(), entry.getValue(), null);
			}
		}
	}

	/**
	 * Is the property one that is not processed for bean diffing.
	 * @param bean the bean
	 * @param propertyName the property name
	 * @return true if it's the class property or others that will cause infinite loops
	 */
	private static boolean isSkippedProperty(Object bean, String propertyName)
	{
		return "class".equals(propertyName)
			|| ((bean instanceof LinkedList) && ("first".equals(propertyName) || "last".equals(propertyName)));
	}

	/**
	 * Should we continue to drill down into this bean?
	 * @param object the object to check
	 * @return true if this object is an instance of Boolean, Character, Class, Number, String, java.util.Date
	 */
	private static boolean drillDown(Object object)
	{
		return !((object instanceof String) || (object instanceof Boolean) || (object instanceof Character)
			|| (object instanceof Number) || (object instanceof Class) || (object instanceof Date));
	}

	/**
	 * Returns a message detailing a bean being created.
	 * @param difference the list of differeces
	 * @param prefix message prefix
	 * @param newBean the object created
	 */
	private static void getBeanAddedMessage(List difference, String prefix, Object newBean)
	{
		StringBuffer message = new StringBuffer();
		if (StringUtils.isNotEmpty(prefix))
		{
			message.append(prefix);
		}
		message.append(" set to '").append(newBean).append("'");
		difference.add(message.toString());
	}

	/**
	 * Returns a message explaining the change(s) of two Objects.
	 * @param difference the list of differeces
	 * @param prefix message prefix
	 * @param oldValue the first Object in the comparison
	 * @param newValue the second Object in the comparison
	 */
	private static void getBeanChangedMessage(List difference, String prefix, Object oldValue, Object newValue)
	{
		StringBuffer message = new StringBuffer();
		if (!StringUtils.isEmpty(prefix))
		{
			message.append(prefix);
		}
		message.append(" changed from '").append(oldValue);
		message.append("' to '").append(newValue).append("'");
		difference.add(message.toString());
	}

	/**
	 * Returns a message explaining what bean was removed.
	 * @param difference the list of differeces
	 * @param prefix message prefix
	 */
	private static void getRemovedBeanMessage(List difference, String prefix, Object oldBean)
	{
		StringBuffer message = new StringBuffer();
		if (StringUtils.isNotEmpty(prefix))
		{
			message.append(prefix);
		}
		message.append(" '").append(oldBean).append("' removed");
		difference.add(message.toString());
	}
}

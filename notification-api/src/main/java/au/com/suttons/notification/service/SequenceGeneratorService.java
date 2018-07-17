package au.com.suttons.notification.service;


import au.com.suttons.notification.resource.bean.SequenceGeneratorBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

@Stateless
public class SequenceGeneratorService extends BaseService
{

    private static final Logger logger = LoggerFactory.getLogger(SequenceGeneratorService.class);

    public SequenceGeneratorBean generateSequenceNumberByName(String name)
    {
        Long sequenceNo = this.sequenceGeneratorDao.generateSequence(name);

        SequenceGeneratorBean bean = new SequenceGeneratorBean();
        bean.setSequenceNumber(sequenceNo);

        return bean;
    }

}

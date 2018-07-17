package au.com.suttons.notification.service;


import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.suttons.notification.data.dao.TemplateDao;
import au.com.suttons.notification.data.entity.TemplateEntity;
import au.com.suttons.notification.mapper.TemplateMapper;
import au.com.suttons.notification.model.TemplateSearchParameters;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.TemplateBean;
import au.com.suttons.notification.resource.page.Page;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.resource.page.Specification;
import au.com.suttons.notification.validator.TemplateValidator;
import au.com.suttons.notification.validator.ValidationHelper;

@Stateless
public class TemplateService extends BaseService
{

	private @EJB TemplateValidator templateValidator;

    private static final Logger logger = LoggerFactory.getLogger(TemplateService.class);

    public BaseCollectionBean<TemplateBean> getTemplates(Pageable pageable, TemplateSearchParameters searchParameters, RequestBean requestBean) throws Exception
    {
        Specification<TemplateEntity> spec = ((TemplateDao.TemplateCriteria)this.templateDao.getCriteria()).byParameters(searchParameters);
        Page<TemplateEntity> page = this.templateDao.getPage(pageable, spec, TemplateEntity.class);

        return new TemplateMapper(requestBean).toCollectionBean(page);
    }

    public TemplateBean getTemplateById(long id, RequestBean requestBean)
    {
        TemplateEntity entity = this.templateDao.findById(id);
        return entity != null ? new TemplateMapper(requestBean).toBean(entity) : null;
    }

    public BaseCollectionBean<TemplateBean> getTemplatesByLowerAccessLevel(Pageable pageable, List<TemplateSearchParameters.LowerAccessLevelRange> ranges, RequestBean requestBean) throws Exception
    {
        Specification<TemplateEntity> spec = ((TemplateDao.TemplateCriteria)this.templateDao.getCriteria()).byLowerAccessLevel(ranges);
        Page<TemplateEntity> page = this.templateDao.getPage(pageable, spec, TemplateEntity.class);

        return new TemplateMapper(requestBean).toCollectionBean(page);
    }

    public TemplateBean saveTemplate(TemplateBean templateBean, RequestBean requestBean)
    {
        TemplateEntity templateEntity = null;
        Long id = templateBean.getId();

        if (id != null) {
        	templateEntity = this.templateDao.findOne(id);
            ValidationHelper.getInstance().validateNotFound(templateEntity, "Template ID: " + id);

        }

        // Validate unique template name
        this.templateValidator.onSave(templateBean, templateDao, requestBean);

        TemplateMapper templateMapper = new TemplateMapper(requestBean);
        templateEntity = templateMapper.toEntity(templateBean, templateEntity);
        templateEntity = this.templateDao.saveAndFlush(templateEntity);

        return templateMapper.toBean(templateEntity);
    }

}

package uk.co.gosseyn.xanax.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import uk.co.gosseyn.xanax.service.PathFinderService;

public abstract class GameObject {
    private static ApplicationContext applicationContext;
    protected static PathFinderService pathFinderService;

    public static void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = applicationContext;
        pathFinderService = (PathFinderService) context.getBean("pathFinderService");
    }
}

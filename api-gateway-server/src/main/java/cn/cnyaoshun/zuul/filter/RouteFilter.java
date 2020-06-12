package cn.cnyaoshun.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;


@Configuration
@RefreshScope
public class RouteFilter extends ZuulFilter {
    @Value("${zuul.prefix}")
    private String prefix;

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        String serviceId = (String) RequestContext.getCurrentContext().get("proxy");
        RequestContext context = RequestContext.getCurrentContext();
        Object originalRequestPath = context.get(FilterConstants.REQUEST_URI_KEY);
        String modifiedRequestPath = prefix+"/"+serviceId+originalRequestPath;
        context.put(FilterConstants.REQUEST_URI_KEY, modifiedRequestPath);
        return null;
    }
}

package ru.jakesmokie.spectre.providers;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.*;

import javax.ws.rs.core.HttpHeaders;
import java.util.Collections;
import java.util.List;

public class CacheFilterFactory implements ResourceFilterFactory {
    private static final List<ResourceFilter> NO_CACHE_FILTER = Collections.<ResourceFilter>singletonList(new CacheResponseFilter("no-cache"));

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        CacheControlHeader cch = am.getAnnotation(CacheControlHeader.class);
        if (cch == null) {
            return NO_CACHE_FILTER;
        } else {
            return Collections.singletonList(new CacheResponseFilter(cch.value()));
        }
    }

    private static class CacheResponseFilter implements ResourceFilter, ContainerResponseFilter {
        private final String headerValue;

        CacheResponseFilter(String headerValue) {
            this.headerValue = headerValue;
        }

        @Override
        public ContainerRequestFilter getRequestFilter() {
            return null;
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return this;
        }

        @Override
        public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
            response.getHttpHeaders().putSingle(HttpHeaders.CACHE_CONTROL, headerValue);
            return response;
        }
    }
}
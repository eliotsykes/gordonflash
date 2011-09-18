package com.jetbootlabs.gordonflash;

import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.codehaus.groovy.grails.web.metaclass.RedirectDynamicMethod;
import org.codehaus.groovy.grails.web.servlet.FlashScope;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes;
import org.codehaus.groovy.grails.web.servlet.GrailsFlashScope;
import org.codehaus.groovy.grails.web.util.WebUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Protects Flash Scope from being cleared out by:
 *   - requests for static resources
 *   - AJAX requests
 *   - non-HTML responses
 */
public class GordonFlashFilter extends OncePerRequestFilter {

    public static final String FLASH_SCOPE = GrailsApplicationAttributes.FLASH_SCOPE;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        GordonFlashScope fs = getOrCreateFlashScope(request);
        filterChain.doFilter(request, response);
        if (!flashScopeShouldHaveBeenCleared(request, response)) {
            fs.previous();
        }
    }

    private boolean flashScopeShouldHaveBeenCleared(HttpServletRequest request, HttpServletResponse response) {
        return isRedirect(request) || (isDynamicHtmlResponse(request, response) && !isAjaxRequest(request));
    }

    private boolean isRedirect(HttpServletRequest request) {
        return request.getAttribute(RedirectDynamicMethod.GRAILS_REDIRECT_ISSUED) != null;
    }

    private boolean isDynamicHtmlResponse(HttpServletRequest request, HttpServletResponse response) {
        return isHtmlResponse(response) && !isRequestForStaticFile(request);
    }

    private boolean isHtmlResponse(HttpServletResponse response) {
        String contentType = response.getContentType();
        return contentType != null && contentType.startsWith("text/html");
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    private boolean isRequestForStaticFile(HttpServletRequest request) {
        String filePath = request.getServletPath().replaceFirst("/", "");
        boolean isRequestForStaticFile = false;
        if (StringUtils.hasLength(filePath)) {
            Resource resource = ApplicationHolder.getApplication().getParentContext().getResource(filePath);
            isRequestForStaticFile = null != resource && resource.exists();
        }
        return isRequestForStaticFile;
    }

    /**
     * Logic for this borrowed from org.codehaus.groovy.grails.web.servlet.DefaultGrailsApplicationAttributes#getFlashScope()
     * @param request
     * @return
     */
    private GordonFlashScope getOrCreateFlashScope(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        FlashScope fs;
        if (session != null) {
            fs = (FlashScope)session.getAttribute(FLASH_SCOPE);
        }
        else {
            fs = (FlashScope)request.getAttribute(FLASH_SCOPE);
        }
        if (fs == null || !(fs instanceof GordonFlashScope)) {
            fs = new GordonFlashScope();
            if (session!=null) {
                session.setAttribute(FLASH_SCOPE,fs);
            }
            else {
                request.setAttribute(FLASH_SCOPE,fs);
            }
        }
        return (GordonFlashScope) fs;
    }
}

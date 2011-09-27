package com.jetbootlabs.gordonflash;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.codehaus.groovy.grails.web.metaclass.RedirectDynamicMethod;
import org.codehaus.groovy.grails.web.servlet.FlashScope;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Protects Flash Scope from being cleared out by:
 *   - requests for static resources
 *   - AJAX requests
 *   - non-HTML responses
 *   - HTML responses that have an error status code
 *   - Explicitly protected requests (see PROTECT_FLASH_SCOPE)
 */
public class GordonFlashFilter extends OncePerRequestFilter {

    //
    /**
     * For special cases not covered by default protection, you can set a request attribute with this key to true to
     * force flash scope not to be wiped during the request.
     */
    public static final String PROTECT_FLASH_SCOPE = "gordonflash.PROTECT_FLASH_SCOPE";
    public static final String FLASH_SCOPE = GrailsApplicationAttributes.FLASH_SCOPE;
    private static final Log log = LogFactory.getLog("gordonflash.GordonFlashFilter");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        GordonFlashServletResponse wrappedResponse = new GordonFlashServletResponse(response);
        GordonFlashScope fs = getOrCreateFlashScope(request);
        filterChain.doFilter(request, wrappedResponse);
        if (log.isDebugEnabled()) {
            log.debug("Request servlet path: " + request.getServletPath());
        }
        if (flashScopeShouldHaveBeenCleared(request, wrappedResponse)) {
            fs.nextThatWorks();
        }
    }

    private boolean flashScopeShouldHaveBeenCleared(HttpServletRequest request, GordonFlashServletResponse response) {
        boolean shouldClear = !isFlashScopeExplicitlyProtected(request) && (isRedirect(request) || (isDynamicHtmlResponse(request, response) && !isAjaxRequest(request) && !response.isErrorResponse()));
        if (log.isDebugEnabled()) {
            log.debug("Flash scope should be cleared? " + shouldClear);
        }
        return shouldClear;
    }

    private boolean isFlashScopeExplicitlyProtected(HttpServletRequest request) {
        return Boolean.TRUE == request.getAttribute(PROTECT_FLASH_SCOPE);
    }

    private boolean isRedirect(HttpServletRequest request) {
        return request.getAttribute(RedirectDynamicMethod.GRAILS_REDIRECT_ISSUED) != null;
    }

    private boolean isDynamicHtmlResponse(HttpServletRequest request, GordonFlashServletResponse response) {
        return isHtmlResponse(response) && !isRequestForStaticFile(request);
    }

    private boolean isHtmlResponse(GordonFlashServletResponse response) {
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

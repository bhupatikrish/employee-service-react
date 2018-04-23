package org.krishna.demo.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.krishna.demo.model.Metadata;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class DefaultErrorMessage extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
    	Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);
    	Map<String, Object> response = new HashMap<String, Object>();
        Metadata metadata = new Metadata();
        
        metadata.setStatus((Integer) errorAttributes.get("status"));
        metadata.setError((String) errorAttributes.get("error"));
        metadata.setMessage((String) errorAttributes.get("message"));
        response.put("metadata", metadata);
        
        return response;
    }
}

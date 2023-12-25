package com.sip.syshumres_apirest.security;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.sip.syshumres_entities.dtos.UserTokenExtraDTO;
import com.sip.syshumres_exceptions.JwtNotFoundException;
import com.sip.syshumres_exceptions.UserTokenExtraNotFoundException;

import java.text.ParseException;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenDecoder {
	
	public static final String EXTRA1 = "idBranchOffice";
	public static final String EXTRA2 = "seeAllBranchs";

    public String getSubjectFromToken(String jwtToken) throws ParseException, JwtNotFoundException {
        String tokenWithoutPrefix = jwtToken.replace("Bearer ", "");
        if (tokenWithoutPrefix == null || tokenWithoutPrefix.equals("")) {
    		throw new JwtNotFoundException();
    	}
        // Parser token JWT
        JWT jwt = JWTParser.parse(tokenWithoutPrefix);

        // Get attr
        return jwt.getJWTClaimsSet().getSubject();
    }
    
    public String getExtraFromToken(String jwtToken, String extra) 
    		throws ParseException, JwtNotFoundException {
    	String tokenWithoutPrefix = jwtToken.replace("Bearer ", "");
    	if (tokenWithoutPrefix == null || tokenWithoutPrefix.equals("") 
    			|| extra == null || extra.equals("")) {
    		throw new JwtNotFoundException();
    	}
    	// Parser token JWT
        JWT jwt = JWTParser.parse(tokenWithoutPrefix);
        
        // Get attr
        return (String) jwt.getJWTClaimsSet().getClaim(extra);
    }
    
    public UserTokenExtraDTO getExtraFromToken(String jwtToken) throws ParseException
    , UserTokenExtraNotFoundException, JwtNotFoundException {
    	String tokenWithoutPrefix = jwtToken.replace("Bearer ", "");
    	if (tokenWithoutPrefix == null || tokenWithoutPrefix.equals("")) {
    		throw new JwtNotFoundException();
    	}
    	// Parser token JWT
        JWT jwt = JWTParser.parse(tokenWithoutPrefix);
       
        Long idBranchOffice = null;
        Object vClaim = jwt.getJWTClaimsSet().getClaim(EXTRA1);
        if (vClaim instanceof Long) {
        	idBranchOffice = (Long) vClaim;
        	if (idBranchOffice <= 0L) {
        		throw new UserTokenExtraNotFoundException();
    		}
        } else {
        	throw new UserTokenExtraNotFoundException();
        }
        
        Boolean seeAllBranchs = false;
        Object vClaim2 = jwt.getJWTClaimsSet().getClaim(EXTRA2);
        if (vClaim2 instanceof Boolean) {
        	seeAllBranchs = (Boolean) vClaim2;
        }
                
        // Get attr
        return new UserTokenExtraDTO(idBranchOffice, seeAllBranchs);
    }
        
}


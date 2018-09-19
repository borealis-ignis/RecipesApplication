package com.recipes.appl.converter;

import java.util.List;

/**
 * @author Kastalski Sergey
 */
public interface DtoDboConverter<T, B> {
	
    T convertDboToDto(B dbo);
    
    B convertDtoToDbo(T dto);
    
    List<T> convertDboToDto(List<B> dboList);
    
    List<B> convertDtoToDbo(List<T> dtoList);
    
}

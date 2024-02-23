//package com.github.backend.converter;
//
//import com.github.backend.web.entity.enums.MateCareStatus;
//import org.springframework.core.convert.converter.Converter;
//
//import java.io.IOException;
//
//public class StringToEnumConverter implements Converter<String, MateCareStatus> {
//
//    @Override
//    public MateCareStatus convert(String source) {
//        try{
//            return MateCareStatus.valueOf(source.toUpperCase());
//        } catch(IllegalArgumentException e) {
//            return null;
//        }
//    }
//}

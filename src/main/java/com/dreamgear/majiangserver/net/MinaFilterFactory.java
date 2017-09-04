/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dreamgear.majiangserver.net;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 *过滤工厂，设置编码类和解码类
 */
public class MinaFilterFactory implements ProtocolCodecFactory {

    private ProtocolEncoder encoder ;
    private ProtocolDecoder decoder ;
    
    public MinaFilterFactory(){
    	encoder = new MinaCMDEncoder();
    	decoder = new MinaCMDDecoder();
    }

    public ProtocolEncoder getEncoder(IoSession is) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession is) throws Exception {
        return decoder;
    }

}

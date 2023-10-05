package com.noodles.crawler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.noodles.crawler.entity.RequestInfo;
import com.noodles.crawler.mapper.RequestInfoMapper;
import com.noodles.crawler.service.RequestInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Noodles
 * @since 2023-09-15
 */
@Service
public class RequestInfoServiceImpl extends ServiceImpl<RequestInfoMapper, RequestInfo> implements RequestInfoService {

}

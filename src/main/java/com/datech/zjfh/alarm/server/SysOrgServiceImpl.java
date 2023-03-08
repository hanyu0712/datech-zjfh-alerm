package com.datech.zjfh.alarm.server;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datech.zjfh.alarm.entity.SysOrgEntity;
import com.datech.zjfh.alarm.mapper.SysOrgMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysOrgServiceImpl extends ServiceImpl<SysOrgMapper, SysOrgEntity> {
    public String getOrgFullName(Integer orgId) {
        String fullName = getNameRecursion(orgId);
        if (StringUtils.isNotBlank(fullName)) {
            fullName = fullName.substring(0, fullName.length() - 1);
        }
        return fullName;
    }

    private String getNameRecursion(Integer orgId) {
        if (orgId == null)
            return "";
        LambdaQueryWrapper<SysOrgEntity> orgQueryWrapper = Wrappers.lambdaQuery();
        orgQueryWrapper.eq(SysOrgEntity::getId, orgId);
        SysOrgEntity org = this.getOne(orgQueryWrapper);
        if (org != null ) {
            return getNameRecursion(org.getPid()) + org.getName() + "-";
        }
        return "";
    }
}

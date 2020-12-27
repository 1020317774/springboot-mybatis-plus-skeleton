package com.knox.uranus.modules.ums.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knox.uranus.common.exception.Asserts;
import com.knox.uranus.modules.ums.dto.UmsAdminUserRegisterDTO;
import com.knox.uranus.modules.ums.dto.UpdateAdminPasswordDTO;
import com.knox.uranus.modules.ums.mapper.UmsAdminLoginLogMapper;
import com.knox.uranus.modules.ums.mapper.UmsAdminMapper;
import com.knox.uranus.modules.ums.mapper.UmsResourceMapper;
import com.knox.uranus.modules.ums.mapper.UmsRoleMapper;
import com.knox.uranus.modules.ums.model.*;
import com.knox.uranus.modules.ums.service.UmsAdminCacheService;
import com.knox.uranus.modules.ums.service.UmsAdminRoleRelationService;
import com.knox.uranus.modules.ums.service.UmsAdminService;
import com.knox.uranus.security.domain.AdminUserDetails;
import com.knox.uranus.security.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台管理员管理Service实现类
 *
 * @author knox
 * @date 2020/08/26
 */
@Service
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdminUser> implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UmsAdminLoginLogMapper loginLogMapper;

    /**
     * 用户缓存
     */
    @Autowired
    private UmsAdminCacheService adminCacheService;

    @Autowired
    private UmsAdminRoleRelationService adminRoleRelationService;

    @Autowired
    private UmsRoleMapper roleMapper;

    @Autowired
    private UmsResourceMapper resourceMapper;

    @Override
    public UmsAdminUser getAdminByUsername(String username) {
        UmsAdminUser admin = adminCacheService.getAdmin(username);
        if (!ObjectUtils.isEmpty(admin)) {
            return admin;
        }
        QueryWrapper<UmsAdminUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdminUser::getUsername, username);
        List<UmsAdminUser> adminList = list(wrapper);
        if (adminList != null && adminList.size() > 0) {
            admin = adminList.get(0);
            adminCacheService.setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public UmsAdminUser register(UmsAdminUserRegisterDTO dto) {
        // 查询是否有相同用户名的用户
        QueryWrapper<UmsAdminUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdminUser::getUsername, dto.getUsername());
        List<UmsAdminUser> umsAdminUserList = list(wrapper);
        if (umsAdminUserList.size() > 0) {
            Asserts.fail("帐号已存在");
        }

        // 数据封装
        UmsAdminUser umsAdminUser = new UmsAdminUser();
        BeanUtils.copyProperties(dto, umsAdminUser);
        if (StrUtil.isEmpty(dto.getAlias())) {
            umsAdminUser.setAlias("用户" + RandomUtil.randomString(8).toUpperCase());
        }
        umsAdminUser.setCreateTime(new Date())
                .setStatus(true);

        // 将密码进行加密操作
        String encodePassword = passwordEncoder.encode(umsAdminUser.getPassword());
        umsAdminUser.setPassword(encodePassword);
        baseMapper.insert(umsAdminUser);
        // 不返回敏感数据
        umsAdminUser.setPassword("");
        return umsAdminUser;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                Asserts.fail("密码不正确");
            }
            if (!userDetails.isEnabled()) {
                Asserts.fail("帐号已被禁用");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            updateLoginTimeByUsername(username);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    /**
     * 添加登录记录
     *
     * @param username 用户名
     */
    private void insertLoginLog(String username) {
        UmsAdminUser admin = getAdminByUsername(username);
        if (admin == null) {
            return;
        }
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        loginLogMapper.insert(loginLog);
    }

    /**
     * 根据用户名修改登录时间
     */
    private void updateLoginTimeByUsername(String username) {
        UmsAdminUser record = new UmsAdminUser();
        record.setLoginTime(new Date());
        QueryWrapper<UmsAdminUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdminUser::getUsername, username);
        update(record, wrapper);
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public Page<UmsAdminUser> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<UmsAdminUser> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UmsAdminUser> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<UmsAdminUser> lambda = wrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(UmsAdminUser::getUsername, keyword);
            lambda.or().like(UmsAdminUser::getAlias, keyword);
        }
        return page(page, wrapper);
    }

    @Override
    public boolean update(String id, UmsAdminUser admin) {
        admin.setId(id);
        UmsAdminUser rawAdmin = getById(id);
        if (rawAdmin.getPassword().equals(admin.getPassword())) {
            //与原加密密码相同的不需要修改
            admin.setPassword(null);
        } else {
            //与原加密密码不同的需要加密修改
            if (StrUtil.isEmpty(admin.getPassword())) {
                admin.setPassword(null);
            } else {
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
        }
        boolean success = updateById(admin);
        adminCacheService.delAdmin(id);
        return success;
    }

    @Override
    public boolean delete(String id) {
        adminCacheService.delAdmin(id);
        boolean success = removeById(id);
        adminCacheService.delResourceList(id);
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(String adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        // 先删除原来角色
        QueryWrapper<UmsAdminRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdminRoleRelation::getAdminId, adminId);
        adminRoleRelationService.remove(wrapper);

        // 分配新角色
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationService.saveBatch(list);
        }

        // 删除角色资源缓存
        adminCacheService.delResourceList(adminId);
        return count;
    }

    @Override
    public List<UmsRole> getRoleList(String adminId) {
        return roleMapper.getRoleList(adminId);
    }

    @Override
    public List<UmsResource> getResourceList(String adminId) {
        List<UmsResource> resourceList = adminCacheService.getResourceList(adminId);
        if (CollUtil.isNotEmpty(resourceList)) {
            return resourceList;
        }
        resourceList = resourceMapper.getResourceList(adminId);
        if (CollUtil.isNotEmpty(resourceList)) {
            adminCacheService.setResourceList(adminId, resourceList);
        }
        return resourceList;
    }

    @Override
    public int updatePassword(UpdateAdminPasswordDTO dto) {
        if (StrUtil.isEmpty(dto.getUsername())
                || StrUtil.isEmpty(dto.getOldPassword())
                || StrUtil.isEmpty(dto.getNewPassword())) {
            return -1;
        }
        QueryWrapper<UmsAdminUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdminUser::getUsername, dto.getUsername());
        List<UmsAdminUser> adminList = list(wrapper);
        if (CollUtil.isEmpty(adminList)) {
            return -2;
        }
        UmsAdminUser umsAdminUser = adminList.get(0);
        if (!passwordEncoder.matches(dto.getOldPassword(), umsAdminUser.getPassword())) {
            return -3;
        }
        umsAdminUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        updateById(umsAdminUser);
        adminCacheService.delAdmin(umsAdminUser.getId());
        return 1;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        //获取用户信息
        UmsAdminUser admin = getAdminByUsername(username);
        if (admin != null) {
            List<UmsResource> resourceList = getResourceList(admin.getId());
            return new AdminUserDetails(admin, resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}

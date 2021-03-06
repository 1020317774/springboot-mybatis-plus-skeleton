package com.knox.uranus.modules.ums.service;

import com.knox.uranus.modules.ums.model.UmsAdminUser;
import com.knox.uranus.modules.ums.model.UmsResource;

import java.util.List;

/**
 * 后台用户缓存管理Service
 *
 * @author knox
 * @date 2020/3/13
 */
public interface UmsAdminCacheService {

    /**
     * 设置缓存后台用户信息
     */
    void setAdmin(UmsAdminUser admin);

    /**
     * 删除后台用户缓存
     */
    void delAdmin(String adminId);

    /**
     * 获取缓存后台用户信息
     */
    UmsAdminUser getAdmin(String username);

    /**
     * 获取缓存后台用户资源列表
     */
    List<UmsResource> getResourceList(String adminId);

    /**
     * 设置后台后台用户资源列表
     */
    void setResourceList(String adminId, List<UmsResource> resourceList);


    /**
     * 删除后台用户资源列表缓存
     */
    void delResourceList(String adminId);

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     */
    void delResourceListByRole(Long roleId);

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     */
    void delResourceListByRoleIds(List<Long> roleIds);

    /**
     * 当资源信息改变时，删除资源项目后台用户缓存
     */
    void delResourceListByResource(Long resourceId);

}

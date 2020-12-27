package com.knox.uranus.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knox.uranus.modules.ums.model.UmsResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 后台资源表 Mapper 接口
 * </p>
 *
 * @author knox
 * @since 2020-08-21
 */
@Mapper
@Repository
public interface UmsResourceMapper extends BaseMapper<UmsResource> {

    /**
     * 获取用户所有可访问资源
     *
     * @param adminId 用户ID
     * @return 用户资源列表
     */
    List<UmsResource> getResourceList(@Param("adminId") String adminId);

    /**
     * 根据角色ID获取资源
     *
     * @param roleId 角色ID
     * @return 用户资源列表
     */
    List<UmsResource> getResourceListByRoleId(@Param("roleId") Long roleId);

}

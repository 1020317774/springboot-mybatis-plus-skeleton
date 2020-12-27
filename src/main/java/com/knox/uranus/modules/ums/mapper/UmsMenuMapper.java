package com.knox.uranus.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knox.uranus.modules.ums.model.UmsMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 后台菜单表 Mapper 接口
 * </p>
 *
 * @author knox
 * @since 2020-08-21
 */
@Mapper
@Repository
public interface UmsMenuMapper extends BaseMapper<UmsMenu> {

    /**
     * 根据后台用户ID获取菜单
     */
    List<UmsMenu> getMenuList(@Param("adminId") String adminId);

    /**
     * 根据角色ID获取菜单
     */
    List<UmsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);

}

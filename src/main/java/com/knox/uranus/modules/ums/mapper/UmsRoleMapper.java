package com.knox.uranus.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knox.uranus.modules.ums.model.UmsRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 Mapper 接口
 * </p>
 *
 * @author knox
 * @since 2020-08-21
 */
@Mapper
@Repository
public interface UmsRoleMapper extends BaseMapper<UmsRole> {

    /**
     * 获取用户所有角色
     */
    List<UmsRole> getRoleList(@Param("adminId") Long adminId);

}

package com.knox.uranus.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knox.uranus.modules.ums.model.UmsAdminRoleRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 后台用户和角色关系表 Mapper 接口
 * </p>
 *
 * @author knox
 * @since 2020-08-21
 */
@Mapper
@Repository
public interface UmsAdminRoleRelationMapper extends BaseMapper<UmsAdminRoleRelation> {

}

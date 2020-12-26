package com.knox.uranus.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knox.uranus.modules.ums.model.UmsRoleResourceRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 后台角色资源关系表 Mapper 接口
 * </p>
 *
 * @author knox
 * @since 2020-08-21
 */
@Mapper
@Repository
public interface UmsRoleResourceRelationMapper extends BaseMapper<UmsRoleResourceRelation> {

}

package com.knox.uranus.modules.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.knox.uranus.modules.ums.dto.UmsAdminUserRegisterDTO;
import com.knox.uranus.modules.ums.dto.UpdateAdminPasswordDTO;
import com.knox.uranus.modules.ums.model.UmsAdminUser;
import com.knox.uranus.modules.ums.model.UmsResource;
import com.knox.uranus.modules.ums.model.UmsRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * 后台管理员管理Service
 *
 * @author knox
 * @date 2020/08/26
 */
public interface UmsAdminService extends IService<UmsAdminUser> {
    /**
     * 根据用户名获取后台管理员
     *
     * @param username 用户名
     * @return
     */
    UmsAdminUser getAdminByUsername(String username);

    /**
     * 注册功能
     *
     * @param umsAdminUserRegisterDTO 用户注册数据
     * @return
     */
    UmsAdminUser register(UmsAdminUserRegisterDTO umsAdminUserRegisterDTO);

    /**
     * 登录功能
     *
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username, String password);

    /**
     * 刷新token的功能
     *
     * @param oldToken 旧的token
     * @return
     */
    String refreshToken(String oldToken);

    /**
     * 根据用户名或昵称分页查询用户
     *
     * @param keyword  关键词
     * @param pageNum  页码
     * @param pageSize 数据
     * @return
     */
    Page<UmsAdminUser> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     *
     * @param id    用户ID
     * @param admin 用户对象
     * @return
     */
    boolean update(String id, UmsAdminUser admin);

    /**
     * 删除指定用户
     *
     * @param id 用户ID
     * @return
     */
    boolean delete(String id);

    /**
     * 修改用户角色关系
     *
     * @param adminId 用户ID
     * @param roleIds 角色ID
     * @return
     */
    int updateRole(String adminId, List<Long> roleIds);

    /**
     * 获取用户对于角色
     *
     * @param adminId 用户ID
     * @return
     */
    List<UmsRole> getRoleList(String adminId);

    /**
     * 获取指定用户的可访问资源
     *
     * @param adminId 用户ID
     * @return
     */
    List<UmsResource> getResourceList(String adminId);

    /**
     * 修改密码
     *
     * @param adminPasswordDTO 密码
     * @return
     */
    int updatePassword(UpdateAdminPasswordDTO adminPasswordDTO);

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return
     */
    UserDetails loadUserByUsername(String username);
}

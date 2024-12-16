package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.dao.UserRoleMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import com.xxxx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 用户登录
     * @param userName
     * @param userPwd
     */
    public UserModel userLogin(String userName, String userPwd) {
        //1、参数判断，判断用户名和密码是否为空
        checkLoginParams(userName, userPwd);
        //2、调用数据访问层
        User user = userMapper.queryUserByName(userName);
        //3、判断用户对象是否为空
        AssertUtil.isTrue(user == null,"用户名不存在");
        //4、判断密码是否正确
        checkUserPwd(userPwd, user.getUserPwd());

        //返回构建的用户对象
        return buildUserInfo(user);
    }

    /**
     * 构建需要返回给客户端的用户对象
     * @param user
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 判断密码是否正确
     * @param userPwd 用户输入的密码
     * @param pwd 数据库中的密码
     */
    private void checkUserPwd(String userPwd, String pwd) {
        //将客户端传递过来的密码进行加密（先将客户端传递过来的密码进行加密，再与数据库中查询到的密码进行比较）
        userPwd = Md5Util.encode(userPwd);
        //判断密码是否相等
        AssertUtil.isTrue(!userPwd.equals(pwd),"用户密码不正确");
    }

    /**
     * 登录参数校验
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        //验证用户姓名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        //验证用户密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
    }

    /**
     * 修改密码
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional
    public void updatePassword(Integer userId, String oldPwd, String newPwd, String repeatPwd) {
        //1、通过用户id查询用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        //2、判断待更新记录是否存在
        AssertUtil.isTrue(null == user, "待更新记录不存在");
        //3、参数效验
        checkPasswordParams(user, oldPwd, newPwd, repeatPwd);
        //4、设置新密码（进行加密）
        user.setUserPwd(Md5Util.encode(newPwd));
        //5、执行更新操作，判断受影响的函数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败");
    }

    /**
     * 修改密码参数判断
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        //判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码不能为空");
        //判断原始密码是否正确（注意：数据库中用户密码是加密的）
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不正确");
        //判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空");
        //判断新密码与原始密码是否相同
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原始密码相同");
        //判断确认密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"确认密码不能为空");
        //判断确认密码与新密码是否一致
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"确认密码与新密码不一致");
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String,Object>> queryAllSales() {
        return userMapper.queryAllSales();
    }

    /**
     * 添加操作
     * @param user
     */
    @Transactional
    public void addUser(User user) {
        //1、参数校验
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),null);
        //2、设置相关参数的默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //设置默认密码（需要进行加密）
        user.setUserPwd(Md5Util.encode("123456"));
        //3、执行添加操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.insertSelective(user) < 1,"添加失败");

        /**
         * 用户角色关联
         *  需要的数据：
         *      用户id：通过用户对象获取 userId
         *      角色id：通过用户对象获取 roleIds
         */
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 用户角色关联
     *  添加操作
     *      原始角色不存在
     *          1、不添加新的角色记录         不需要操作中间表（t_user_role）
     *          2、添加新的角色记录          给指定用户绑定相关的角色记录
     *
     *  更新操作
     *      原始角色不存在
     *          1、不添加新的角色记录         不需要操作中间表
     *          2、添加新的角色记录          给指定用户绑定相关的角色记录
     *      原始角色存在
     *          1、添加新的角色记录          判断已有的角色记录不添加；添加没有的角色记录
     *          2、清空所有的角色记录         删除用户绑定的角色记录
     *          3、移除部分角色记录           删除不存在的角色记录，存在的角色记录依然保留
     *          4、移除部分角色，添加新的角色   删除不存在的角色记录，存在的角色记录保留，添加新的角色记录
     *
     *  解决方案：如何进行角色分配呢？？
     *      先判断用户对应的角色记录是否存在
     *          将用户原有的角色记录全部删除，然后添加新的角色记录。
     *
     *  删除操作
     *      删除指定用户绑定的角色记录
     * @param userId 用户id
     * @param roleIds 角色id
     */
    private void relationUserRole(Integer userId, String roleIds) {
        //1、通过userId查询对应的角色记录
        Integer count = userRoleMapper.countUserRoleByUserId(userId);
        //2、判断角色记录是否存在
        if(count > 0) {
            //删除用户原有的全部角色记录
            AssertUtil.isTrue(userRoleMapper.deleteByUserId(userId) != count,"用户角色分配失败");
        }
        //判断角色id是否存在，如果存在，则添加该用户对应的角色记录
        if(!StringUtils.isBlank(roleIds)) {
            //将用户角色数据设置到记录中，执行批量添加
            List<UserRole> list = new ArrayList<>();
            //将roleIds字符串转换成数组
            String[] roleIdArray = roleIds.split(",");
            //遍历数组，得到对应的用户角色对象，并设置到集合中
            for(String roleId : roleIdArray) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                //将对象设置到集合中
                list.add(userRole);
            }
            //批量添加用户角色记录
            AssertUtil.isTrue(userRoleMapper.insertBatch(list) != list.size(),"用户角色分配失败");
        }

    }

    /**
     * 添加用户的参数校验
     * @param userName
     * @param email
     * @param phone
     */
    private void checkUserParams(String userName, String email, String phone, Integer userId) {
        //判断用户名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        //判断用户名的唯一性（通过该用户名去查数据库）
        User temp = userMapper.queryUserByName(userName);
        AssertUtil.isTrue(temp != null && !(temp.getId().equals(userId)),"用户名已存在，请重新输入");
        //判断邮箱是否为空
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
        //判断手机号
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确");
    }

    /**
     * 修改操作
     * @param user
     */
    @Transactional
    public void updateUser(User user) {
        //1、参数校验
        AssertUtil.isTrue(null == user.getId(),"待更新记录不存在");
        AssertUtil.isTrue(null == userMapper.selectByPrimaryKey(user.getId()),"待更新记录不存在");
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),user.getId());;
        //2、设置相关参数的默认值
        user.setUpdateDate(new Date());
        //3、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1,"用户更新失败");

        /* 用户角色关联 */
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 批量删除操作
     * @param ids
     */
    @Transactional
    public void deleteUser(Integer[] ids) {
        //1、判断id是否为空
        AssertUtil.isTrue(null == ids && ids.length < 1, "待删除记录不存在");
        //2、执行删除操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.deleteBatch(ids) != ids.length, "数据删除失败");
    }

    /**
     * 查询所有的客户经理
     * @return
     */
    public List<Map<String, Object>> queryAllCustomerManagers() {
        return userMapper.queryAllCustomerManagers();
    }
}

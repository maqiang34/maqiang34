package com.smartcold.manage.cold.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smartcold.manage.cold.dao.UserMapper;
import com.smartcold.manage.cold.entity.comm.ACLAdminNode;
import com.smartcold.manage.cold.entity.comm.UserEntity;
import com.smartcold.manage.cold.util.EncodeUtil;
import com.smartcold.manage.cold.util.ResponseData;
import com.smartcold.manage.cold.util.StringUtil;
import com.smartcold.manage.cold.util.TableData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


/*
 * 追溯
 */
@RestController
@RequestMapping(value="/i/user")
public class User_Controller {

    @Resource
    private UserMapper userMapper;




    /**
     *
     * @param request
     * @param user
     * @return
     */
    @RequestMapping(value = "/login",method= RequestMethod.POST)
    @ResponseBody
    public Object login(HttpServletRequest request, UserEntity user) {
        try {
            if(StringUtil.isNull(user.getName())||StringUtil.isNull(user.getPwd())||!StringUtil.vserToken( user.getType())){ return ResponseData.newFailure("请输入完整信息");}
            user.setPwd( EncodeUtil.encodeByMD5(user.getPwd()));
            UserEntity loguser = userMapper.login(user);
            if (loguser != null&&loguser.getId()!=0) {
                String token = StringUtil.getUUIDMD5pwd(user.getPwd(),null);
                loguser.setPwd(null);
                loguser.setToken(token);
                request.getSession().setAttribute("user", loguser);
                userMapper.updateUser(new UserEntity(loguser.getId(),1));
                return	ResponseData.newSuccess(loguser);
            }
            return ResponseData.newFailure("用户名或者密码不正确！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.newFailure("数据连接异常！请稍后重试！");
        }
    }

    @RequestMapping(value = "/isLogin", method = RequestMethod.GET)
    @ResponseBody
    public Object isLogin(HttpSession session) {
        UserEntity user = (UserEntity)session.getAttribute("user");
        if(user!=null&&user.getId()!=0){
            return ResponseData.newSuccess(user);
        }
        return ResponseData.newFailure();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public Object logout(HttpSession session) {
        session.removeAttribute("user");
        return true;
    }
    @RequestMapping(value = "/vistUserName")
    @ResponseBody
    public Object vistUserName(HttpSession session,String username) {
       int count= this.userMapper.vistUserName(username);
        return true;
    }


    /**
     *
     * @param session
     * @param page
     * @param rows
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/findUserList", method = RequestMethod.POST)
    @ResponseBody
    public Object findUserList(HttpSession session, Integer page,Integer rows,Integer available,String keyword,String coleam,  String  colval) {
       UserEntity userEntity= (UserEntity) session.getAttribute("user");
       if(userEntity!=null&&userEntity.getType()==2){
           page = page == null? 1:page;
           rows = rows==null? 10:rows;
           PageHelper.startPage(page, rows);
            new PageInfo<UserEntity>();
           Page<UserEntity> allUser =userMapper.findUserList(available,keyword,coleam,colval);
           return TableData.newSuccess(allUser);
       }else{
           return TableData.newSuccess();
       }
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public Object updateUser(HttpSession session,UserEntity user) {
        UserEntity userEntity= (UserEntity) session.getAttribute("user");
        if(userEntity!=null&&userEntity.getType()==2){
            if(user.getId()!=0){
                this.userMapper.updateUser(user);
            }
            return  ResponseData.newSuccess();
        }else{
            return  ResponseData.newFailure("操作失败！");
        }
    }

    @RequestMapping(value = "/deleteByUid", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteByUid(HttpSession session,int  [] ids) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null&&userEntity.getType()==2&&ids!=null&&ids.length>0){
                this.userMapper.delUserByIds(StringUtil.getIdS(ids));
                return  ResponseData.newSuccess();
            }else{
                return  ResponseData.newFailure("非法操作");
            }
        } catch (Exception e) {
            return  ResponseData.newFailure("操作失败！");
        }
    }


    @RequestMapping(value = "/findUserById")
    @ResponseBody
    public Object findUserById(HttpSession session,int id) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null&&userEntity.getType()==2){
                return  ResponseData.newSuccess( this.userMapper.finedUserById(id));
            }else{
                return  ResponseData.newFailure("非法操作");
            }
        } catch (Exception e) {
            return  ResponseData.newFailure("操作失败！");
        }
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public Object addUser(HttpSession session,UserEntity user) {
        try {
            UserEntity userEntity= (UserEntity) session.getAttribute("user");
            if(userEntity!=null&&userEntity.getType()==2){
                if(user.getId()==0){
                    user.setPwd(EncodeUtil.encodeByMD5(user.getPwd()));
                    this.userMapper.addUser(user);
                }else{
                   this.userMapper.updateUser(user);
                }

                return  ResponseData.newSuccess( );
            }else{
                return  ResponseData.newFailure("非法操作");
            }
        } catch (Exception e) {
            return  ResponseData.newFailure("操作失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getUserMenu")
    public Object getUserMenu(HttpSession session) {
        UserEntity user = (UserEntity)session.getAttribute("user");
        if(user!=null&&user.getId()!=0){
            String[]aclml={"0-1234","","0-012345"};
            return getml(aclml[user.getType()]);
        }
        return "false";
    }

    public List<ACLAdminNode> getml(String alc) {
        List<ACLAdminNode> ml = getMenu();
        List<ACLAdminNode> nml=new ArrayList<ACLAdminNode>();
        String[] split = alc.split("-");
        String pacl=split[0];
        String[] cacl=split[1].split("@");
        int index=0,mlsize=ml.size();
        for (char fix : pacl.toCharArray()) {
            index=Integer.parseInt(fix+"");
            if(index<mlsize){
                ACLAdminNode aclAdminNode = ml.get(index);
                ACLAdminNode cloneNode = new ACLAdminNode(aclAdminNode.getMenuid(),aclAdminNode.getIcon(),aclAdminNode.getMenuname());
                List<ACLAdminNode> child = aclAdminNode.getChild();
                List<ACLAdminNode> nchild = new ArrayList<ACLAdminNode>();
                if(StringUtil.isnotNull(cacl[index])){
                    for (char sfix : cacl[index].toCharArray()) {
                         index=Integer.parseInt(sfix+"");
                        if(index<child.size()){
                            nchild.add(child.get(index));
                        }
                    }
                    cloneNode.setChild(nchild);
                    nml.add(cloneNode);
                }

            }
        }
        return nml;
    }

    private  List<ACLAdminNode> getMenu() {
        List<ACLAdminNode> ml=new ArrayList<ACLAdminNode>();
        ACLAdminNode pml = new ACLAdminNode("0","main_platform",      "管理中心");
        List<ACLAdminNode> mlList0=new ArrayList<ACLAdminNode>();
        mlList0.add(new ACLAdminNode("0_0","icon-user",      "用户管理",      "user_manage.html?v=1.1"       ));
        mlList0.add(new ACLAdminNode("0_1","main_company",      "企业中心",      "comp_manage.html?v=1.1"        ));
        mlList0.add(new ACLAdminNode("0_2","icon-coldcf",     "基地管理",      "grow_manage.html?v=1.1"     ));
        mlList0.add(new ACLAdminNode("0_3","main_ad",     "商品管理",         "good_manage.html?v=1.1"     ));
        mlList0.add(new ACLAdminNode("0_4","main_share",     "追溯管理",         "trace_manage.html?v=1.1"     ));
        mlList0.add(new ACLAdminNode("0_5","icon-authe",     "真伪管理",         "auth_manage.html?v=1.1"     ));
        pml.setChild(mlList0);ml.add(pml);

        pml  = new ACLAdminNode("1"  ,"main_coun",  "网站统计");
        List<ACLAdminNode> mlList2=new ArrayList<ACLAdminNode>();
        mlList2.add(new ACLAdminNode("1_0","icon-role",    "网站统计", "" ));
        mlList2.add(new ACLAdminNode("1_1","icon-role",    "轨迹分析", "" ));
        mlList2.add(new ACLAdminNode("1_2","icon-role",    "用户体验",  "" ));
        pml.setChild(mlList2);
        ml.add(pml);

        pml  = new ACLAdminNode("2"  ,"main_sys",     "系统管理");
        List<ACLAdminNode> mlList3=new ArrayList<ACLAdminNode>();
        mlList3.add(new ACLAdminNode("2_0","icon-role",    "系统消息", "sys_msg.html" ));
        mlList3.add(new ACLAdminNode("2_1","icon-role",    "系统状态", "sys_state.html" ));
        pml.setChild(mlList3);    ml.add(pml);
        return ml;
    }



}

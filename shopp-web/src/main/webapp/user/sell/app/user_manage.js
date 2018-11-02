var queryParams = {page: null, rows: null,  keyword: null};
//======================================================================格式化col=======================================================
var col_audit = function (i) { switch(i){case -1:return '未通过';case 0:return '待审核';default: return '通过';}};

//======================================================================菜单事件function=======================================================
//批量删除用户
var deleteUsers = function () {
    var userIDs =  getTableCheckedID();
    if (userIDs.length > 0) {
        $.messager.confirm('删除确认', '你确认要<er>删除</er>这<er>'+userIDs.length+'</er>条用户信息吗?', function (r) {
            if (r) {  $.ajax({  type: 'POST', url: '/i/user/deleteByUid.do', traditional:true,   data: { ids: userIDs },success: function (data) { if(data.status==200){ reloaddata();}else{ $.messager.alert('错误', '删除用户失败！', 'error'); } }}); }
        });
    } else {  $.messager.alert('删除用户', '您还没有选择用户哦', 'info'); }

};
//添加用户
function adduser(ioc,tit){
    $('#userForm').form('clear');
    $("#user_id").val(0);
    $("#rad_type_0,#read_audit_1").click();
    $('#input_userpwd').validatebox({  required:true});
    $('#username').validatebox({prompt:'请输入用户名',required:true,validType:{length:[3,16],remote:['/i/user/vistUserName.do','username']}});
    $('#userdialog').dialog({title:'添加用户信息',iconCls:'user_add',closed: false});
}
//tool
function edituser(){var selusers=getTableChecked();if(selusers.length>0){var user=selusers[0];user.password=null;editByuser( user);}else{ alert_infomsg("请选择一个用户进行操作");}}
//保存用户
function saveUser(){
    if(queryParams.lock){ return;}
    var userfrom=$('#userForm');
    if (! userfrom.form('validate')) {  return;}else{ queryParams.lock=true; }
    var data=$('#userForm').serialize();
    $.ajax({type: 'POST', url: '/i/user/addUser.do',data:data,
        success: function(data) {
            queryParams.lock=false;
            reloaddata();
            if(data.status==200){$('#userdialog').dialog({closed: true});}else{alert_errmsg('保存失败！请稍后重试！');  }
        }
    });
}

//编辑用户
function editByuser(user){
    $('#userForm').form('clear');
    $('#input_userpwd').validatebox({  required:false});
    $('#username').validatebox({prompt:'请输入用户名',required:true,validType:{length:[3,16]}});
    $('#userForm').form('load',user);
    $('#userdialog').dialog({title:'修改用户信息',iconCls:'user_edit',closed: false});
}
//=====================修改审核状态===============================
function user_audit(id,audit,username){$("#user_auditForm").form('load',{id:id,oldaudit:audit,audit:audit,username:username});$('#user_auditdialog').dialog({closed: false});}
function user_upaudit(){$('#user_auditdialog').dialog({closed: true});var id=$("#user_auditForm input[name='id']").val(),oldaudit=$("#user_auditForm input[name='oldaudit']").val(), audit=$("#user_auditForm input[name='audit']:checked").val();if(oldaudit!=audit&&id!=""){
    $.post('/i/user/updateUser.do', {'id': id, 'available': audit}, function () { reloaddata();});
}}
//=====================修改用户级别===============================
function user_level(id,level,username){$("#user_levelForm").form('load',{id:id,level:level,oldlevel:level,username:username});$('#user_leveldialog').dialog({closed: false});}
function user_uplevel(){$('#user_leveldialog').dialog({closed: true});var id=$("#user_levelForm input[name='id']").val(),oldlevel=$("#user_levelForm input[name='oldlevel']").val(), level=$("#user_levelForm input[name='level']:checked").val();if(level!=oldlevel&&id!=""){$.post('../../i/user/changeLevel', {'userID': id, 'vipType': level}, function () { reloaddata();});}}




function onDblClickRow(index, field) {editByuser(field);}//双击编辑用户信息
//======================================================================格式化col=======================================================
function init_table() {
//	var fottol  ={buttons: [{text:'添加用户',iconCls:'user_add',handler:adduser},{text:'编辑用户',iconCls:'user_edit',handler:edituser},{text:'删除用户',iconCls:'user_del',handler:deleteUsers},"-"]};
    var col = [[
        {field: 'ck', checkbox: true},
        {field: 'id', title: 'ID', sortable: true},
        {field: 'name', title: '用户名', width: 80, align: 'center', sortable: true},
        {field: 'telephone', title: '手机号', width: 80, align: 'center', sortable: true},
        {field: 'email', title: '邮箱', width: 80, align: 'center', sortable: true},
        {field: 'available', title: '状态', width: 80, align: 'center', sortable: true, formatter: col_audit},
        {field: 'lgcount', title: '登录次数', width: 80, align: 'center', sortable: true},
        {field: 'addtime', title: '注册时间', width: 80, align: 'center', sortable: true, formatter: tool.col_format},
    ]];
    initTable("用户管理", "icon-user", "POST", "/i/user/findUserList.do", queryParams, '#user_filter', null, col, true, onDblClickRow);
//    objTable.datagrid({singleSelect: true});//设置为单选
    crspsh();
}

//======================================================================初始化数据=======================================================
//初始化数据
$().ready(function () {
    init_table();
    $('#sel_user_auto').combobox({  onChange:function(val){  queryParams.available=val;   reloaddata(queryParams);}  });
});
//======================================================================初始化数据=======================================================

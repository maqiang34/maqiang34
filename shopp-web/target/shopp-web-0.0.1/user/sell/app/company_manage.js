var queryParams = {page: null, rows: null,  keyword: null},imginfo={delimg:[],delimgid:[],addimg:[] ,oldimg:[],i:0};
//======================================================================格式化col=======================================================

//======================================================================菜单事件function=======================================================
function newinginfo(){ imginfo={delimg:[],delimgid:[],addimg:[] ,oldimg:[],i:0};}

//工具栏--》删除
var tl_deleteData = function () {
    var oids =  getTableCheckedID();
    if (oids.length > 0) {
        $.messager.confirm('删除确认', '你确认要<er>删除</er>这<er>'+oids.length+'</er>条信息吗?', function (r) {
            if (r) {  $.ajax({  type: 'POST', url: '/i/company/deleteByCid.do', traditional:true,   data: { ids: oids },success: function (data) { if(data.status==200){ reloaddata();}else{ alert_errmsg( '删除失败!请稍后重试!'); } }}); }
        });
    } else {  alert_infomsg( '您还没有选择删除的信息哦'); }

};
//工具栏--》编辑
function tl_editData() {
    var selusers = getTableChecked();
    if (selusers.length > 0) {
        var data = selusers[0];
        editFromData(data);
    } else {
        alert_infomsg("请选择一个条数据进行操作");
    }
}
//工具栏--》添加
function tl_addDData(){

    newinginfo();
    $("#comp_imgList").empty();
    $("#img_logo").attr("src","");
    $('#from_data').form('clear');
    $('#datadialog').dialog({title:'添加企业信息',iconCls:'main_company',closed: false});
}
//======================================================================菜单事件function======================================================================================================================

//编辑信息
function editFromData(data){
    newinginfo();
    initimgs(data.id,1);
    $('#from_data').form('clear');
    // $('#username').validatebox({prompt:'请输入用户名',required:true,validType:{length:[3,16]}});
    $("#img_logo").attr("src",sys.imgrooturl+data.logo)
    $('#from_data').form('load',data);
    $('#datadialog').dialog({title:'编辑企业信息',iconCls:'main_company',closed: false});
}
//保存信息
function saveFromData(){
    if(imginfo.lock){ return;}
    var fromdata=$('#from_data');
    if (! fromdata.form('validate')) {  return;}else{  imginfo.lock=true; }
    var vo = {},formdata = new FormData(),parnArray =fromdata.serializeArray();
    $.each(parnArray, function (index, item) {
            formdata.append(item.name,item.value);
    });
   var logo=$("#file_logo")[0].files[0];
   if(logo){  formdata.append("logoimg",logo); }
   if(imginfo.addimg.length>0){
       $.each(imginfo.addimg, function (index, item) {
               formdata.append('file'+index, item);
       });
   }
   if(imginfo.delimgid.length>0){//删除图片的信息
       formdata.append('fileids', imginfo.delimgid);
       formdata.append('fileurls', imginfo.delimg);
   }
    $.ajax({
        url: "/i/company/addComp.do",
        data: formdata,
        traditional:true,
        processData: false,
        contentType: false,
        type: 'POST',
        success: function (data) {
            imginfo.lock=false;
            $('#datadialog').dialog({closed: true})
            if(data.status!=200){ alert_errmsg(data.message);}
            reloaddata();
        }
    });
}




//======================================================================格式化col=======================================================
function onDblClickRow(index, field) {editFromData(field);}//双击编辑数据详细信息

function init_table() {
//	var fottol  ={buttons: [{text:'添加用户',iconCls:'user_add',handler:addDromData},{text:'编辑用户',iconCls:'user_edit',handler:edituser},{text:'删除用户',iconCls:'user_del',handler:deleteUsers},"-"]};
    var col = [[
        {field: 'ck', checkbox: true},
        {field: 'id', title: 'ID', sortable: true},
        {field: 'name', title: '名称', width: 80, align: 'center', sortable: true},
        {field: 'logo', title: 'logo', width: 80, align: 'center', sortable: true,formatter:tool.col_img},
        {field: 'address', title: '地址', width: 80, align: 'center', sortable: true},
        {field: 'telephone', title: '联系电话', width: 80, align: 'center', sortable: true},
        {field: 'time', title: '注册时间', width: 80, align: 'center', sortable: true, formatter: tool.col_format},
    ]];
    initTable("企业管理", "main_company", "POST", "/i/company/findCompList.do", queryParams, '#user_filter', null, col, true, onDblClickRow);
//    objTable.datagrid({singleSelect: true});//设置为单选
    crspsh();
}

//======================================================================初始化数据=======================================================
//初始化数据
$().ready(function () {
    init_table();
});
//======================================================================初始化数据=======================================================




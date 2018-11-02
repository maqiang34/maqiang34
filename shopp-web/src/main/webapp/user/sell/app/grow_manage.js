var queryParams = {page: null, rows: null,  keyword: null},imginfo={delimg:[],delimgid:[],addimg:[] ,oldimg:[],i:0};
//======================================================================菜单事件function=======================================================
function newinginfo(){ imginfo={delimg:[],delimgid:[],addimg:[] ,oldimg:[],i:0};}

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
    $("#input_id,#input_uid").val(null);
    $('#datadialog').dialog({title:'添加基地信息',iconCls:'main_company',closed: false});
}
//工具栏--》删除
var tl_deleteData = function () {
    var oids =  getTableCheckedID();
    if (oids.length > 0) {
        $.messager.confirm('删除确认', '你确认要<er>删除</er>这<er>'+oids.length+'</er>条信息吗?', function (r) {
            if (r) {  $.ajax({  type: 'POST', url: '/i/grow/deleteByids.do', traditional:true,   data: { ids: oids },success: function (data) { if(data.status==200){ reloaddata();}else{ $.messager.alert('错误', '删除失败！', 'error'); } }}); }
        });
    } else {  alert_infomsg( '您还没有选择删除的信息哦'); }

};
//======================================================================菜单事件function======================================================================================================================

//编辑信息
function editFromData(data){
    newinginfo();
    initimgs(data.id,2);
    $('#from_data').form('clear');
    $("#img_logo").attr("src",sys.imgrooturl+data.logo)
    $('#from_data').form('load',data);
    $('#datadialog').dialog({title:'编辑基地信息',iconCls:'main_company',closed: false});
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
        url: "/i/grow/add.do",
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
    var col = [[
        {field: 'ck', checkbox: true},
        {field: 'id', title: 'ID', sortable: true},
        {field: 'name', title: '基地名称', width: 80, align: 'center', sortable: true},
        {field: 'compName', title: '企业名称', width: 80, align: 'center', sortable: true},
        {field: 'address', title: '地址', width: 80, align: 'center', sortable: true},
        {field: 'area', title: '面积', width: 80, align: 'center', sortable: true},
    ]];
    initTable("基地管理", "icon-coldcf", "POST", "/i/grow/list.do", queryParams, '#user_filter', null, col, true, onDblClickRow);
//    objTable.datagrid({singleSelect: true});//设置为单选
    crspsh();
}

//======================================================================初始化数据=======================================================
//初始化数据
$().ready(function () {
    init_table();
});
//======================================================================初始化数据=======================================================

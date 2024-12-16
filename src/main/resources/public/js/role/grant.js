$(function () {
    //加载树形结构
    loadModuleData();
});

//定义属性结构的对象
var zTreeObj;

/**
 * 加载资源树形列表
 */
function loadModuleData() {
    //1、配置信息对象  zTree中的参数配置
    var setting = {
        check: {    //使用复选框
            enable: true
        },
        data: {     //使用简单的json格式
            simpleData: {
                enable: true
            }
        },
        callback: {  //绑定函数
            //onCheck函数：当 checkbox 或 radio 被选中或被取消选中时触发的函数
            onCheck: zTreeOnCheck
        }
    }
    //2、通过ajax查询资源列表
    $.ajax({
        type: "get",
        url: ctx + "/module/queryAllModules",
        //在查询所有的资源列表时，传递角色id，查询当前角色对应的已经授权的资源
        data: {
            roleId: $("[name='roleId']").val()
        },
        dataType: "json",
        success:function (data) {
            // data就是查询到的资源列表
            // 加载zTree树形结构
            zTreeObj = $.fn.zTree.init($("#test1"), setting, data);
        }
    });

    /**
     * 当 checkbox或 radio 被选中或被取消选中时触发的函数
     * @param event
     * @param treeId
     * @param treeNode
     */
    function zTreeOnCheck(event, treeId, treeNode) {
        // alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);

        //getCheckedNodes：获取所有被勾选的节点集合。如果checked=true，表示获取被勾选的节点；如果checked=false，则表示获取未勾选的节点
        var nodes = zTreeObj.getCheckedNodes(true);
        // console.log(nodes);

        //获取所有资源的id值 mIds=1&mIds=2&mIds=3
        //判断并遍历选中的节点集合
        if(nodes.length > 0) {
            //定义资源id
            var mIds = "mIds=";
            //遍历节点集合，获取资源的id
            for(var i=0; i<nodes.length; i++) {
                if(i < nodes.length - 1) {
                    mIds += nodes[i].id + "&mIds=";
                } else {
                    mIds += nodes[i].id;
                }
            }
            console.log(mIds);

            //获取需要授权的角色id（从页面隐藏域中获取）
            var roleId = $("[name='roleId']").val();

            //发送ajax请求，执行角色的授权操作
            $.ajax({
                type: "post",
                url: ctx + "/role/addGrant",
                data: mIds+"&roleId="+roleId,
                dataType: "json",
                success:function (data) {
                    console.log(data);
                }
            });
        }
    };
}
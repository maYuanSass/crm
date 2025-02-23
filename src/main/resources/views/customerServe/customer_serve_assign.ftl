<!DOCTYPE html>
<html>
<head>
    <title>服务分配</title>
    <#include "../common.ftl">
</head>
<body class="childrenBody">

<form class="layui-form">
    <blockquote class="layui-elem-quote quoteBox">
        <form class="layui-form">
            <div class="layui-inline">
                <div class="layui-input-inline">
                    <input type="text" name="customer"
                           class="layui-input
					searchVal" placeholder="客户"/>
                </div>
                <div class="layui-input-inline">
                    <select name="type" id="type">
                        <option value="">请选择</option>
                        <option value="6">咨询</option>
                        <option value="7">投诉</option>
                        <option value="8">建议</option>
                    </select>
                </div>

                <a class="layui-btn search_btn" data-type="reload"><i
                            class="layui-icon">&#xe615;</i> 搜索</a>
            </div>
        </form>
    </blockquote>

    <table id="customerServeList" class="layui-table" lay-filter="customerServes"></table>

    <script type="text/html" id="toolbarDemo">

    </script>

    <script id="customerServeListBar" type="text/html">
        <a href="javascript:;" class="layui-btn layui-btn-normal layui-btn-xs" lay-event="assign">分配</a>
    </script>

</form>
<script type="text/javascript" src="${ctx}/js/customerServe/customer.serve.assign.js"></script>

</body>
</html>
/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: Administrator
 * Created: 2017/06/07
 * Description:
 *
 */

package com.inter3i.test.base;

import com.alibaba.fastjson.JSONObject;
import com.inter3i.base.expression.ExpressionEngine;
import com.inter3i.base.expression.entity.ExpressionEntity;

import java.util.ArrayList;
import java.util.List;

public class ExpressionTest {
    public static void main(String[] args) {

        Character a = Character.valueOf('中');
        Character b = Character.valueOf('中');
        System.out.println(a == b ? "等于" : "不等于");
        System.out.println(a.equals(b) ? "等于" : "不等于");
        Long long1 = Long.valueOf(8);
        Long long2 = Long.valueOf(8);
        System.out.println(long1 == long2 ? "等于" : "不等于");

        // ((student.age ++ > 18) ? chengnian:weichengnian)== "chengnian" && (student.sex == male)

        ExpressionEntity expressionEntity = new ExpressionEntity();//总表达式
        expressionEntity.setType(ExpressionEntity.OPERATOR_TYPE_LOGIC);
        expressionEntity.setOperator("and");
        List<ExpressionEntity> childExpression = new ArrayList<>(2);//总表达式的容器，用来装子表达式
        expressionEntity.setChildExpressions(childExpression);//总表达式设置容器

        ExpressionEntity firstExpressionEntity = new ExpressionEntity();//第一个子表达式实体
        childExpression.add(firstExpressionEntity);//总表达式的容器 添加第一个表达式实体

        firstExpressionEntity.setOperator("==");
        firstExpressionEntity.setType(ExpressionEntity.OPERATOR_TYPE_BINARY);
        List<ExpressionEntity> childsOfFirst = new ArrayList<>(2);//定义第一个子表达式容器
        firstExpressionEntity.setChildExpressions(childsOfFirst);//第一个子表达式实体设置自己的容器

        ExpressionEntity sanMu = new ExpressionEntity();//三目表达式
        childsOfFirst.add(sanMu);//三目加入第一个子表达式
        sanMu.setType(ExpressionEntity.OPERATOR_TYPE_TERNARY);
        sanMu.setOperator("?");

        List<ExpressionEntity> childsOfSanmu = new ArrayList<>(3);//三目子逻辑

        sanMu.setChildExpressions(childsOfSanmu);//【三目表达式】设置【子逻辑】

        //student.age ++
        ExpressionEntity danmuExpress = new ExpressionEntity();//单目表达式
        danmuExpress.setOperator("++");//运算符
        danmuExpress.setType(ExpressionEntity.OPERATOR_TYPE_UNARY);//类型是单目

        ExpressionEntity.ValueTermEntity valueTermEntity = new ExpressionEntity.ValueTermEntity();//运算数定义
        valueTermEntity.setDataType("int");
        valueTermEntity.setValueType("pathExpress");
        valueTermEntity.setValue("student.age");
        valueTermEntity.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);

        List<ExpressionEntity> childsOfDanmu = new ArrayList<>(1);//单目【子表达式容器】,即运算数容器定义
        childsOfDanmu.add(valueTermEntity);//子表达式容器 添加 【运算数student.age】

        danmuExpress.setChildExpressions(childsOfDanmu);//单目表达式 设置 子表达容器【运算数：student.age】
        //end of student.age ++

        // (student.age ++ > 18) ? chengnian:weichengnian; start
        ExpressionEntity moreThan18Ofsanmu = new ExpressionEntity();//大于18的表达式
        moreThan18Ofsanmu.setType(ExpressionEntity.OPERATOR_TYPE_BINARY);//双目表达式
        moreThan18Ofsanmu.setOperator("$gt");//关系运算符

        List<ExpressionEntity> childsOfLogic = new ArrayList<>(2);//大于18岁的 子逻辑容器
        childsOfLogic.add(danmuExpress);//子逻辑列表添加 单目表达式，即student.age ++
        moreThan18Ofsanmu.setChildExpressions(childsOfLogic);//大于18的表达式设置【子逻辑容器】

        childsOfSanmu.add(moreThan18Ofsanmu);//三目表达式子逻辑 添加大于18的判断

        // 18 constants 即判断标准
        ExpressionEntity.ValueTermEntity isAdult = new ExpressionEntity.ValueTermEntity();//成年判断标准
        isAdult.setDataType("int");
        isAdult.setValueType("constants");
        isAdult.setValue("18");
        isAdult.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfLogic.add(isAdult);//大于18岁的判断逻辑

        //false value of 三目
        ExpressionEntity.ValueTermEntity falseValueOfDanMu = new ExpressionEntity.ValueTermEntity();
        falseValueOfDanMu.setDataType("String");
        falseValueOfDanMu.setValueType("constants");
        falseValueOfDanMu.setValue("weichengnian");
        falseValueOfDanMu.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfSanmu.add(falseValueOfDanMu);//三目表达式添加false value of danmu

        //true value of 三目
        ExpressionEntity.ValueTermEntity trueValueOfDanMu = new ExpressionEntity.ValueTermEntity();
        trueValueOfDanMu.setDataType("String");
        trueValueOfDanMu.setValueType("constants");
        trueValueOfDanMu.setValue("chengnian");
        trueValueOfDanMu.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfSanmu.add(trueValueOfDanMu);//三目表达式添加true value of danmu

        //end of  (student.age ++ > 18) ? chengnian:weichengnian;

        // ((student.age ++ > 18) ? chengnian:weichengnian)== "chengnian" start
        ExpressionEntity.ValueTermEntity chengnianBiaoZhun = new ExpressionEntity.ValueTermEntity();
        chengnianBiaoZhun.setDataType("String");
        chengnianBiaoZhun.setValueType("constants");
        chengnianBiaoZhun.setValue("chengnian");
        chengnianBiaoZhun.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);
        childsOfFirst.add(chengnianBiaoZhun);//第一个子表达式加入成年
        // ((student.age ++ > 18) ? chengnian:weichengnian)== "chengnian" end


        // **************** sex == male ***************/
        //sex == male
        ExpressionEntity second = new ExpressionEntity();//第二个子表达式
        second.setOperator("==");
        second.setType(ExpressionEntity.OPERATOR_TYPE_BINARY);


        List<ExpressionEntity> childsOfSecond = new ArrayList<>(2);//第二个子表达式容器
        second.setChildExpressions(childsOfSecond);//第二个子表达式设置容器
        childExpression.add(second);//主表达式容器添加第二个表达式

        ExpressionEntity.ValueTermEntity studentSex = new ExpressionEntity.ValueTermEntity();//性别判断逻辑
        studentSex.setDataType("String");
        studentSex.setValueType("pathExpress");
        studentSex.setValue("student.sex");
        studentSex.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);

        childsOfSecond.add(studentSex);//第二个子表达式容器添加【学生性别判断逻辑】

        ExpressionEntity.ValueTermEntity constantsMale = new ExpressionEntity.ValueTermEntity();//性别表达式判断基准
        constantsMale.setDataType("String");
        constantsMale.setValueType("constants");
        constantsMale.setValue("male");
        constantsMale.setType(ExpressionEntity.OPERATOR_TYPE_VALUE);

        childsOfSecond.add(constantsMale);//第二个子表达式添加学生性别判断基准student.sex == male
        // **************** sex == male ***************/

        ExpressionEngine.Expression expression = ExpressionEntity.conver2Expression(expressionEntity);//总表达式公式
        JSONObject content = new JSONObject();

        JSONObject student = new JSONObject();
        student.put("name", "zhangsan");
        student.put("sex", "male");
        student.put("age", 18);
        content.put("student", student);

        Object result = expression.evel(content);//将数值带入公式，得到结果
        System.out.println("result is :[" + result + "].");
    }
}

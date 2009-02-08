/**
 * 
 */
package org.wltea.expression.op.define;

import org.wltea.expression.IllegalExpressionException;
import org.wltea.expression.datameta.BaseDataMeta;
import org.wltea.expression.datameta.Constant;
import org.wltea.expression.datameta.Reference;
import org.wltea.expression.datameta.BaseDataMeta.DataType;
import org.wltea.expression.op.IOperatorExecution;
import org.wltea.expression.op.Operator;

/**
 * @author 林良益
 * 2009-02-03 
 */
public class Op_SELECT implements IOperatorExecution {

	public static final Operator THIS_OPERATOR = Operator.SELECT;

	/* (non-Javadoc)
	 * @see org.wltea.expression.op.IOperatorExecution#execute(org.wltea.expression.datameta.Constant[])
	 */
	public Constant execute(Constant[] args) throws IllegalExpressionException {
		//参数校验
		if(args == null || args.length != 3){
			throw new IllegalArgumentException("操作符\"" + THIS_OPERATOR.getToken() + "操作缺少参数");
		}		
		Constant first = args[2];
		if(null == first || null == first.getDataValue()){
			//抛NULL异常
			throw new NullPointerException("操作符\"" + THIS_OPERATOR.getToken() + "\"第一参数为空");
		}
		Constant second = args[1];
		if(null == second || null == second.getDataValue()){
			//抛NULL异常
			throw new NullPointerException("操作符\"" + THIS_OPERATOR.getToken() + "\"第二参数为空");
		}		
		Constant third = args[0];
		if(null == third || null == third.getDataValue()){
			//抛NULL异常
			throw new NullPointerException("操作符\"" + THIS_OPERATOR.getToken() + "\"第三参数为空");
		}
		
		//如果第一参数为引用，则执行引用
		if(DataType.DATATYPE_REFERENCE == first.getDataType()){
			Reference firstRef = (Reference)first.getDataValue();
			first = firstRef.execute();
		}
		if( BaseDataMeta.DataType.DATATYPE_BOOLEAN ==  first.getDataType()){
			if(first.getBooleanValue()){
				//选择第二参数
				//如果第二参数为引用，则执行引用
				if(DataType.DATATYPE_REFERENCE == second.getDataType()){
					Reference secondRef = (Reference)second.getDataValue();
					second = secondRef.execute();
				}				
				return second;
			}else{
				//选择第三参数
				//如果第三参数为引用，则执行引用
				if(DataType.DATATYPE_REFERENCE == third.getDataType()){
					Reference thirdRef = (Reference)third.getDataValue();
					third = thirdRef.execute();
				}				
				return third;				
			}
			
		}else{
			//抛异常
			throw new IllegalArgumentException("操作符\"" + THIS_OPERATOR.getToken() + "\"第一参数类型错误");
		}
	}

	/* (non-Javadoc)
	 * @see org.wltea.expression.op.IOperatorExecution#verify(int, org.wltea.expression.datameta.BaseDataMeta[])
	 */
	public Constant verify(int opPositin, BaseDataMeta[] args)
			throws IllegalExpressionException {

		if(args == null){
			throw new IllegalArgumentException("运算操作符参数为空");
		}
		if(args.length != 3){
			//抛异常
			throw new IllegalExpressionException("操作符\"" + THIS_OPERATOR.getToken() + "\"参数个数不匹配"
						, THIS_OPERATOR.getToken()
						, opPositin
					);
		}
		BaseDataMeta first = args[2];
		BaseDataMeta second = args[1];
		BaseDataMeta third = args[0];
		if(first == null || second == null || third == null){
			throw new NullPointerException("操作符\"" + THIS_OPERATOR.getToken() + "\"参数为空");
		}		
		//判定第一参数是否为boolean类型
		if( BaseDataMeta.DataType.DATATYPE_BOOLEAN !=  first.getDataType()){
			throw new IllegalExpressionException("操作符\"" + THIS_OPERATOR.getToken() + "\"参数类型错误"
					, THIS_OPERATOR.getToken()
					, opPositin);
		}
		//判定二，三参数类型是否相同
		if(second.getDataType() == third.getDataType()){
			return new Constant(second.getDataType(), null);
			
		}else if(BaseDataMeta.DataType.DATATYPE_NULL == second.getDataType()){
			return new Constant(third.getDataType(), null);
			
		}else if(BaseDataMeta.DataType.DATATYPE_NULL == third.getDataType()){
			return new Constant(second.getDataType(), null);
			
		}else{
			throw new IllegalExpressionException("操作符\"" + THIS_OPERATOR.getToken() + "\"二，三参数类型不一致"
					, THIS_OPERATOR.getToken()
					, opPositin);
		}
	}

}

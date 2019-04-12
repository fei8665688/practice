package udaf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFParameterInfo;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author phil.zhang
 * @date 2019/1/30
 */
public class MyUDAF extends AbstractGenericUDAFResolver {
    public static final Logger log = LoggerFactory.getLogger(MyUDAF.class);

    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] params) throws SemanticException {
        if (params.length != 2) {
            throw new UDFArgumentException("Need two arguments");
        }
        ObjectInspector oi1 = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(params[0]);
        ObjectInspector oi2 = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(params[1]);
        if (oi1.getCategory() != ObjectInspector.Category.PRIMITIVE
                || oi2.getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentException("Arguments must be PRIMITIE");
        }

        PrimitiveObjectInspector inputOi1 = (PrimitiveObjectInspector) oi1;
        PrimitiveObjectInspector inputOi2 = (PrimitiveObjectInspector) oi2;

        if (inputOi1.getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.DOUBLE) {
            throw new UDFArgumentException("Argument1 must be Double");
        }

        if (inputOi2.getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.INT) {
            throw new UDFArgumentException("Argument2 must be int");
        }

        return new GenericUDAFBucketAverageEvaluator();
    }

    @Override
    public GenericUDAFEvaluator getEvaluator(GenericUDAFParameterInfo info) throws SemanticException {
        return super.getEvaluator(info);
    }

    public static class GenericUDAFBucketAverageEvaluator extends GenericUDAFEvaluator {

        // 确定各个阶段输入输出参数的数据格式
        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {

            // 第一阶段
            if (m == Mode.PARTIAL1) {
                assert (parameters.length == 2);
            }
            return super.init(m, parameters);
        }

        // 保存数据聚集结果的类
        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            return null;
        }

        // 重置聚集结果
        @Override
        public void reset(AggregationBuffer agg) throws HiveException {

        }

        // map阶段，迭代处理输入sql传过来的列数据
        @Override
        public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException {

        }

        // map与combiner结束时返回，得到部分数据聚合结果
        @Override
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            return null;
        }

        // combiner合并map返回的结果，还有reducer合并mapper或combiner返回的结果
        @Override
        public void merge(AggregationBuffer agg, Object partial) throws HiveException {

        }

        // reducer阶段，输出最终结果
        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            return null;
        }
    }
}

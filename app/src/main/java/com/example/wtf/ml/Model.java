package com.example.wtf.ml;

import android.content.Context;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.IOException;

public class Model {
    private Interpreter interpreter;

    private Model(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public static Model newInstance(Context context) throws IOException {
        Interpreter interpreter = new Interpreter(FileUtil.loadMappedFile(context, "model.tflite"));
        return new Model(interpreter);
    }

    public Outputs process(TensorBuffer input) {
        TensorBuffer output = TensorBuffer.createFixedSize(new int[]{1, /* number of classes */}, DataType.FLOAT32);
        interpreter.run(input.getBuffer(), output.getBuffer());
        return new Outputs(output);
    }

    public void close() {
        interpreter.close();
    }

    public class Outputs {
        private final TensorBuffer outputFeature0;

        public Outputs(TensorBuffer outputFeature0) {
            this.outputFeature0 = outputFeature0;
        }

        public TensorBuffer getOutputFeature0AsTensorBuffer() {
            return outputFeature0;
        }
    }
}

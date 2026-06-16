package Control;

import java.io.*;
import java.util.*;
import javax.servlet.ServletContext;

public class EnvConfig {

    // Store .env in WEB-INF to avoid public exposure
    public static File envFile(ServletContext ctx) {
        String path = ctx.getRealPath("/WEB-INF/.env");
        return new File(path);
    }

    public static Properties readEnv(ServletContext ctx) throws IOException {
        Properties p = new Properties();
        File f = envFile(ctx);
        if (f.exists()) {
            try (InputStream in = new FileInputStream(f)) {
                p.load(in);
            }
        }
        return p;
    }

    public static void writeEnv(ServletContext ctx, Properties p) throws IOException {
        File f = envFile(ctx);
        File parent = f.getParentFile();
        if (!parent.exists()) parent.mkdirs();
        try (OutputStream out = new FileOutputStream(f)) {
            p.store(out, "SMTP configuration (do not store production secrets in repo)");
        }
    }
}

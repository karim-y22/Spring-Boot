package at.boot.banner;

import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.util.List;


public class ProdBanner implements Banner {
    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        out.println("               ,--,                                                                                           ");
        out.println("            ,---.'|                                          ,-.----.                 ,----..                 ");
        out.println("  .--.--.   |   | :      ,---,  ,----..                      \\    /  \\  ,-.----.     /   /   \\      ,---,     ");
        out.println(" /  /    '. :   : |   ,`--.' | /   /   \\        ,---,        |   :    \\ \\    /  \\   /   .     :   .'  .' `\\   ");
        out.println("|  :  /`. / |   ' :   |   :  :|   :     :      /_ ./|        |   |  .\\ :;   :    \\ .   /   ;.  \\,---.'     \\  ");
        out.println(";  |  |--`  ;   ; '   :   |  '.   |  ;. /,---, |  ' :        .   :  |: ||   | .\\ :.   ;   /  ` ;|   |  .`\\  | ");
        out.println("|  :  ;_    '   | |__ |   :  |.   ; /--`/___/ \\.  : |        |   |   \\ :.   : |: |;   |  ; \\ ; |:   : |  '  | ");
        out.println(" \\  \\    `. |   | :.'|'   '  ;;   | ;    .  \\  \\ ,' '        |   : .   /|   |  \\ :|   :  | ; | '|   ' '  ;  : ");
        out.println("  `----.   \\'   :    ;|   |  ||   : |     \\  ;  `  ,'        ;   | |`-' |   : .  /.   |  ' ' ' :'   | ;  .  | ");
        out.println("  __ \\  \\  ||   |  ./ '   :  ;.   | '___   \\  \\    '         |   | ;    ;   | |  \\'   ;  \\; /  ||   | :  |  ' ");
        out.println(" /  /`--'  /;   : ;   |   |  ''   ; : .'|   '  \\   |         :   ' |    |   | ;\\  \\\\   \\  ',  / '   : | /  ;  ");
        out.println("'--'.     / |   ,/    '   :  |'   | '/  :    \\  ;  ;         :   : :    :   ' | \\.' ;   :    /  |   | '` ,/   ");
        out.println("  `--'---'  '---'     ;   |.' |   :    /      :  \\  \\        |   | :    :   : :-'    \\   \\ .'   ;   :  .'     ");
        out.println("                      '---'    \\   \\ .'        \\  ' ;        `---'.|    |   |.'       `---`     |   ,.'       ");
        out.println("                                `---`           `--`           `---`    `---'                   '---'          ");
        out.println();
        out.println("***********************************************************************");
        out.println("*                                                                     *");
        out.println("*                  *** PRODUCTION PROFILE ACTIVE ***                  *");
        out.println("*                                                                     *");
        out.println("***********************************************************************");
        out.println();
        out.println(" Spring Boot Version: " + org.springframework.boot.SpringBootVersion.getVersion());
        out.println(" Java Version       : " + System.getProperty("java.version") + " (" + System.getProperty("java.vm.name") + ")");
        out.println(" OS                 : " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        out.println(" CPU Cores          : " + Runtime.getRuntime().availableProcessors());
        out.println(" Max Memory (MB)    : " + (Runtime.getRuntime().maxMemory() / 1024 / 1024));
        out.println(" Active Profile(s)  : " + String.join(", ", environment.getActiveProfiles()));
        out.println(" Build Time         : " + java.time.LocalDateTime.now());
        out.println();

        String serverPort = environment.getProperty("server.port", "8080");
        out.println(" App running on     : http://localhost:" + serverPort);
        out.println();

        // JVM Arguments ausgeben
        List<String> jvmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
        out.println(" JVM Arguments:");
        if (jvmArgs.isEmpty()) {
            out.println("  (keine JVM-Argumente gesetzt)");
        } else {
            for (String arg : jvmArgs) {
                out.println("  " + arg);
            }
        }

//        out.println();
//        out.println(" System Properties:");
//        Properties sysProps = System.getProperties();
//        for (String key : sysProps.stringPropertyNames()) {
//            if (key.startsWith("java.") || key.startsWith("os.") || key.startsWith("user.")) {
//                out.println("  " + key + " = " + sysProps.getProperty(key));
//            }
//        }
//
//        out.println();
//        out.println(" Environment Variables:");
//        Map<String, String> env = System.getenv();
//        env.forEach((k, v) -> out.println("  " + k + " = " + v));

        out.println(" -------------------------------------");
        out.println(" Keep calm and code on! ");
        out.println(" -------------------------------------");
    }
}

package org.mltsaku67.elasticsearch;

import java.lang.reflect.Field;
import java.nio.file.Path;

import org.codelibs.elasticsearch.runner.ElasticsearchClusterRunner;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.common.settings.loader.SettingsLoader;
import org.elasticsearch.common.settings.loader.SettingsLoaderFactory;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.mltsaku67.elasticsearch.annotations.ClusterRunnerSetting;
import org.mltsaku67.elasticsearch.annotations.ClusterSetting;

public class ElasticsearchTestClassRunner extends BlockJUnit4ClassRunner {

    private static final Class<ElasticsearchClusterRunner> RUNNER_CLZZ = ElasticsearchClusterRunner.class;

    private final Class<?> testClass;

    public ElasticsearchTestClassRunner(Class<?> klass)
            throws InitializationError {
        super(klass);
        this.testClass = klass;
    }

    @Override
    public void run(final RunNotifier notifier) {
        Field runnerField = findRunnerClassField();
        if (runnerField == null || getValue(runnerField, RUNNER_CLZZ) != null) {
            super.run(notifier);
        } else {
            ElasticsearchClusterRunner runner = null;
            try {
                runner = new ElasticsearchClusterRunner();
                ClusterRunnerSetting runnerSetting = testClass
                        .getDeclaredAnnotation(ClusterRunnerSetting.class);
                if (runnerSetting != null) {
                    addClusterSetting(runner, runnerSetting);
                    addIndexSettings(runner, runnerSetting);
                    addTypeSettings(runner, runnerSetting);
                }
                setValue(runnerField, null, runner);
                super.run(notifier);
            } finally {
                if (runner != null) {
                    runner.close();
                    runner.clean();
                }
            }
        }
    }

    private void addClusterSetting(ElasticsearchClusterRunner runner,
            ClusterRunnerSetting runnerSetting) {
        ClusterSetting clusterSetting = runnerSetting.clusterSetting();
        SettingsLoader loader = SettingsLoaderFactory
                .loaderFromResource(clusterSetting.value());
        Path resourceFilePath = findFullPath(clusterSetting.value());
        runner.onBuild(new ElasticsearchClusterRunner.Builder() {
            @Override
            public void build(int index, Builder settingsBuilder) {
            }
        }).build();
    }

    private void addIndexSettings(ElasticsearchClusterRunner runner,
            ClusterRunnerSetting runnerSetting) {
    }

    private void addTypeSettings(ElasticsearchClusterRunner runner,
            ClusterRunnerSetting runnerSetting) {
    }

    private Field findRunnerClassField() {
        Field[] fields = testClass.getDeclaredFields();
        for (Field field : fields) {
            if (RUNNER_CLZZ.equals(field.getType())) {
                return field;
            }
        }
        return null;
    }

    private Path findFullPath(String resourcePath) {
        String replaceAll = testClass.getCanonicalName().replaceAll("\\.", "/");
        return null;
    }

    private void setValue(Field field, Object target, Object value) {
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(false);
        }
    }

    private <T> T getValue(Field field, Class<T> clzz) {
        try {
            field.setAccessible(true);
            return clzz.cast(field.get(null));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(false);
        }
    }
}

package com.yammer.metrics.core;


/**
 * A value class encapsulating a metric's owning class and name.
 */
public class MetricName implements Comparable<MetricName> {
    private final String group;
    private final String type;
    private final String name;
    private final String scope;
    private final String mbeanName;

    /**
     * Creates a new {@link MetricName} without a scope.
     *
     * @param klass the {@link Class} to which the {@link Metric} belongs
     * @param name  the name of the {@link Metric}
     */
    public MetricName(Class<?> klass, String name) {
        this(klass, name, null);
    }

    /**
     * Creates a new {@link MetricName} without a scope.
     *
     * @param group the group to which the {@link Metric} belongs
     * @param type  the type to which the {@link Metric} belongs
     * @param name  the name of the {@link Metric}
     */
    public MetricName(String group, String type, String name) {
        this(group, type, name, null);
    }

    /**
     * Creates a new {@link MetricName} without a scope.
     *
     * @param klass the {@link Class} to which the {@link Metric} belongs
     * @param name  the name of the {@link Metric}
     * @param scope the scope of the {@link Metric}
     */
    public MetricName(Class<?> klass, String name, String scope) {
        this(klass.getPackage() == null ? "": klass.getPackage().getName(),
             klass.getSimpleName().replaceAll("\\$$", ""),
             name,
             scope);
    }

    /**
     * Creates a new {@link MetricName} without a scope.
     *
     * @param group the group to which the {@link Metric} belongs
     * @param type  the type to which the {@link Metric} belongs
     * @param name  the name of the {@link Metric}
     * @param scope the scope of the {@link Metric}
     */
    public MetricName(String group, String type, String name, String scope) {
        this(group, type, name, scope, MetricName.createMBeanName(group, type, name, scope));
    }

    /**
     * Creates a new {@link MetricName} without a scope.
     *
     * @param group     the group to which the {@link Metric} belongs
     * @param type      the type to which the {@link Metric} belongs
     * @param name      the name of the {@link Metric}
     * @param scope     the scope of the {@link Metric}
     * @param mbeanName the 'ObjectName', represented as a string, to use when registering the
     *                  mbean.
     */
    public MetricName(String group, String type, String name, String scope, String mbeanName) {
        if (group == null || type == null) {
            throw new IllegalArgumentException("Both group and type need to be specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name needs to be specified");
        }
        this.group = group;
        this.type = type;
        this.name = name;
        this.scope = scope;
        this.mbeanName = mbeanName;
    }

    /**
     * Returns the group to which the {@link Metric} belongs. For class-based metrics, this will be
     * the package name of the {@link Class} to which the {@link Metric} belongs.
     *
     * @return the group to which the {@link Metric} belongs
     */
    public String getGroup() {
        return group;
    }

    /**
     * Returns the type to which the {@link Metric} belongs. For class-based metrics, this will be
     * the simple class name of the {@link Class} to which the {@link Metric} belongs.
     *
     * @return the type to which the {@link Metric} belongs
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the name of the {@link Metric}.
     *
     * @return the name of the {@link Metric}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the scope of the {@link Metric}.
     *
     * @return the scope of the {@link Metric}
     */
    public String getScope() {
        return scope;
    }

    /**
     * Returns {@code true} if the {@link Metric} has a scope, {@code false} otherwise.
     *
     * @return {@code true} if the {@link Metric} has a scope
     */
    public boolean hasScope() {
        return scope != null;
    }

    /**
     * Returns the mbean name for the {@link Metric} identified by this metric name.
     *
     * @return the mbean name
     */
    public String getMBeanName() {
        return mbeanName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final MetricName that = (MetricName) o;
        return mbeanName.equals(that.mbeanName);
    }

    @Override
    public int hashCode() {
        return mbeanName.hashCode();
    }

    @Override
    public String toString() {
        return mbeanName;
    }

    private static String createMBeanName(String group, String type, String name, String scope) {
        final StringBuilder mbeanNameBuilder = new StringBuilder();
        mbeanNameBuilder.append(group);
        mbeanNameBuilder.append(":type=");
        mbeanNameBuilder.append(type);
        if (scope != null) {
            mbeanNameBuilder.append(",scope=");
            mbeanNameBuilder.append(scope);
        }
        if (name.length() > 0) {
            mbeanNameBuilder.append(",name=");
            mbeanNameBuilder.append(name);
        }
        return mbeanNameBuilder.toString();
    }

    @Override
    public int compareTo(MetricName o) {
        return mbeanName.compareTo(o.mbeanName);
    }
}

/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class Parameter {

        public static Parameter Default = new Parameter("default", new String("default"));

        protected String name;
        protected Object value;

        public Parameter(String name, Object value) {
                this.name = name;
                this.value = value;
        }

        public String getName() {
                return name;
        }

        public Object getValue() {
                return value;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((name == null) ? 0 : name.hashCode());
                result = prime * result + ((value == null) ? 0 : value.hashCode());
                return result;
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                Parameter other = (Parameter) obj;
                if (name == null) {
                        if (other.name != null)
                                return false;
                } else if (!name.equals(other.name))
                        return false;
                if (value == null) {
                        if (other.value != null)
                                return false;
                } else if (!value.equals(other.value))
                        return false;
                return true;
        }

}

/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class QueryResultImpl implements QueryResult {

        private List<QueryData> dataList = new ArrayList<QueryData>();

        public void addData(QueryData data) {
                if (data != null)
                        dataList.add(data);
        }

        @Override
        public List<QueryData> getData() {
                return dataList;
        }

}

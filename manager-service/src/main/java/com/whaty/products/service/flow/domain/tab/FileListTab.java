package com.whaty.products.service.flow.domain.tab;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件列表tab
 *
 * @author weipengsen
 */
@Data
public class FileListTab extends AbstractBaseTab<List<Map<String, Object>>> {

    private static final long serialVersionUID = -5920189730899170620L;

    private List<FileItem> items;

    public FileListTab(String title, List<Map<String, Object>> baseInfo) {
        super(title, baseInfo);
    }

    @Data
    private class FileItem implements Serializable {

        private static final long serialVersionUID = 7362078315831845649L;

        private String id;

        private String name;

        private String url;

        private String type;

        private FileItem(Map<String, Object> origin) {
            this(origin.get("id").toString(), (String) origin.get("name"), (String) origin.get("url"));
        }

        private FileItem(String id, String name, String url) {
            this.id = id;
            this.name = name;
            this.url = url;
            this.type = this.name.substring(this.name.lastIndexOf(".") + 1);
        }
    }

    @Override
    public TabType getType() {
        return TabType.FILE_LIST;
    }

    @Override
    public AbstractBaseTab convert(List<Map<String, Object>> origin) {
        this.setItems(origin.stream().map(FileItem::new).collect(Collectors.toList()));
        return this;
    }
}

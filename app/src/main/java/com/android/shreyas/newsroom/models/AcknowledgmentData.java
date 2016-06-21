package com.android.shreyas.newsroom.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcknowledgmentData {

    List<Map<String,?>> list;

    public List<Map<String, ?>> getMoviesList() {
        return list;
    }

    public int getSize(){
        return list.size();
    }

    public HashMap getItem(int i){
        if (i >=0 && i < list.size()){
            return (HashMap) list.get(i);
        } else return null;
    }

    public AcknowledgmentData(){
        String description;
		String title;
		list = new ArrayList<Map<String,?>>();
        //#1-10
		title = "Retrofit Library";
        description = "Copyright 2013 Square, Inc.\n" +
				"\n" +
				"Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
				"you may not use this file except in compliance with the License.\n" +
				"You may obtain a copy of the License at\n" +
				"\n" +
				"   http://www.apache.org/licenses/LICENSE-2.0\n" +
				"\n" +
				"Unless required by applicable law or agreed to in writing, software\n" +
				"distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
				"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
				"See the License for the specific language governing permissions and\n" +
				"limitations under the License.";
		list.add(createMovie(title,description));
		title = "Google Gson";
		description = "Copyright 2008 Google Inc.\n" +
				"\n" +
				"Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
				"you may not use this file except in compliance with the License.\n" +
				"You may obtain a copy of the License at\n" +
				"\n" +
				"    http://www.apache.org/licenses/LICENSE-2.0\n" +
				"\n" +
				"Unless required by applicable law or agreed to in writing, software\n" +
				"distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
				"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
				"See the License for the specific language governing permissions and\n" +
				"limitations under the License.";
		list.add(createMovie(title,description));
		title = "Picasso";
        description = "Copyright 2013 Square, Inc.\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.";
        list.add(createMovie(title,description));
        title ="Circle Image View";
        description = "Copyright 2014 - 2015 Henning Dodenhof\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.";
        list.add(createMovie(title,description));
        title="Android Player";
        description="Copyright 2016 Georgi Eftimov\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.";
        list.add(createMovie(title,description));
        title = "Pull Refresh Layout";
        description  ="The MIT License (MIT)\n" +
                "\n" +
                "Copyright (c) 2014 baoyongzhang\n" +
                "\n" +
                "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
                "of this software and associated documentation files (the \"Software\"), to deal\n" +
                "in the Software without restriction, including without limitation the rights\n" +
                "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                "copies of the Software, and to permit persons to whom the Software is\n" +
                "furnished to do so, subject to the following conditions:\n" +
                "\n" +
                "The above copyright notice and this permission notice shall be included in all\n" +
                "copies or substantial portions of the Software.\n" +
                "\n" +
                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
                "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" +
                "SOFTWARE.";
        list.add(createMovie(title,description));
    }


    private HashMap createMovie(String name, String description) {
        HashMap movie = new HashMap();

        movie.put("name", name);
        movie.put("description", description);
        return movie;
    }
}

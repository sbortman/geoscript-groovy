package geoscript.workspace

import org.junit.Test
import static org.junit.Assert.*
import geoscript.layer.Layer
import geoscript.feature.Field
import geoscript.feature.Schema

/**
 * The Directory UnitTest
 */
class DirectoryTestCase {

    @Test void constructors() {

        File file = new File(getClass().getClassLoader().getResource("110m-admin-0-countries.shp").toURI()).parentFile
        assertNotNull(file)

        Directory dir = new Directory(file)
        assertNotNull(dir)
        assertEquals "Directory", dir.format
        assertEquals "Directory[${file}/]".toString(), dir.toString()

        Directory dir2 = new Directory(file.absolutePath)
        assertNotNull(dir2)
        assertEquals "Directory", dir2.format
        assertEquals "Directory[${file}/]".toString(), dir2.toString()

    }

    @Test void getLayers() {
        File file = new File(getClass().getClassLoader().getResource("110m-admin-0-countries.shp").toURI()).parentFile
        assertNotNull(file)
        Directory dir = new Directory(file)
        assertNotNull(dir)
        assertEquals 1, dir.layers.size()
        assertEquals "[110m-admin-0-countries]", dir.layers.toString()
    }

    @Test void get() {
        File file = new File(getClass().getClassLoader().getResource("110m-admin-0-countries.shp").toURI()).parentFile
        assertNotNull(file)
        Directory dir = new Directory(file)
        assertNotNull(dir)

        Layer layer = dir.get("110m-admin-0-countries")
        assertNotNull(layer)
        assertEquals "110m-admin-0-countries", layer.name
    }

    @Test void create() {
        File file = new File(System.getProperty("java.io.tmpdir"))
        Directory dir = new Directory(file)
        Layer layer = dir.create("points", [new Field("geom","Point","EPSG:4326")])
        assertNotNull(layer)
        assertTrue(new File(file,"points.shp").exists())

        Layer layer2 = dir.create(new Schema("lines", [new Field("geom","Point","EPSG:4326")]))
        assertNotNull(layer2)
        assertTrue(new File(file,"lines.shp").exists())
    }

    @Test void add() {

        File file1 = new File(getClass().getClassLoader().getResource("110m-admin-0-countries.shp").toURI()).parentFile
        Directory dir1 = new Directory(file1)
        Layer layer1 = dir1.get("110m-admin-0-countries")

        File file2 = new File(System.getProperty("java.io.tmpdir"))
        Directory dir2 = new Directory(file2)
        Layer layer2 = dir2.add(layer1, "countries")
        println("${layer2.name} ${layer2.format}")
        assertTrue(new File(file2,"countries.shp").exists())

        dir2.add(layer1)
        assertTrue(new File(file2,"110m-admin-0-countries.shp").exists())

    }

}

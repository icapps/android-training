package com.worldline.nicolaldi.util;

import android.content.Context;

import com.worldline.nicolaldi.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Nicola Verbeeck
 */
@RunWith(MockitoJUnitRunner.class)
public class DatabaseKeyMapperTest {

    @Mock
    Context context;

    @Test
    public void getStringResourceForKey() {
        assertEquals(R.string.item_banana, DatabaseKeyMapper.getStringResourceForKey("banana"));
        assertEquals(R.string.item_apple, DatabaseKeyMapper.getStringResourceForKey("apple"));
        assertEquals(R.string.item_lemon, DatabaseKeyMapper.getStringResourceForKey("lemon"));
        assertEquals(R.string.item_lime, DatabaseKeyMapper.getStringResourceForKey("lime"));
    }

    @Test
    public void getImageResourceForKey() {
        assertEquals(R.drawable.banana, DatabaseKeyMapper.getImageResourceForKey("banana"));
    }

    @Test
    public void loadStringForKey() {
        Mockito.when(context.getString(R.string.item_banana)).thenReturn("Chiquita");

        assertEquals("Chiquita", DatabaseKeyMapper.loadStringForKey(context, "banana"));

        Mockito.verify(context, Mockito.times(1)).getString(Mockito.anyInt());
    }
}